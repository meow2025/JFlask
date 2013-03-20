package in.keepp.jflask;

import in.keepp.jflask.annotations.Intercept;
import in.keepp.jflask.annotations.Route;
import in.keepp.jflask.convetor.ConvertorFactory;
import in.keepp.jflask.exceptions.AbortException;
import in.keepp.jflask.interceptor.Interceptor;
import in.keepp.jflask.interceptor.InterceptorChain;
import in.keepp.jflask.interceptor.InterceptorChainImpl;
import in.keepp.jflask.template.Template;
import in.keepp.jflask.template.TemplateFactory;
import in.keepp.jflask.template.VelocityTemplateFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JFlask {

	private static ThreadLocal<Request> requests = new ThreadLocal<Request>();
	private static ThreadLocal<Response> responses = new ThreadLocal<Response>();
	private static ThreadLocal<Cookies> cookiez = new ThreadLocal<Cookies>();
	private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();

	private static Logger logger = LoggerFactory.getLogger(JFlask.class);
	
	public static Request getRequest() {
		return requests.get();
	}

	public static Response getResponse() {
		return responses.get();
	}
	
	public static Cookies getCookies() {
		return cookiez.get();
	}
	
	public static Session getSession() {
		Session session = sessions.get();
		if(session == null) {
			session = new Session(getRequest());
			sessions.set(session);
		}
		return session;
	}

	public static void abort(int statusCode) {
		throw new AbortException(statusCode);
	}

	public static Template renderTemplate(String filename) {
		return TemplateFactory.getInstance().loadTemplate(filename, null);
	}

	public static Template renderTemplate(String filename, Map<String, Object> model) {
		return TemplateFactory.getInstance().loadTemplate(filename, model);
	}

	private Map<URLExtracter, List<View>> mapping = new HashMap<URLExtracter, List<View>>();
	private List<URLExtracter> extracters = null;
	private Interceptor[] interceptors = null;

	void init(ServletConfig servletConfig) {
		try {
			Config.load(servletConfig);
			scanAnnotations();
			initTemplateFactory();
		} catch (IOException e) {
			logger.info("Error when loading config.");
			e.printStackTrace();
		}
	}

	private void scanAnnotations() {
		scanRoutes();
		scanInterceptors();
	}

	private void initTemplateFactory() {
		VelocityTemplateFactory factory = new VelocityTemplateFactory();
		factory.init();
		TemplateFactory.setTemplateFactory(factory);
	}

	private void scanRoutes() {
		Reflections reflections = new Reflections(Config.get(Config.VIEWS_PREFIX),
				new MethodAnnotationsScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(Route.class);
		Map<Class<?>, List<Method>> temp = new HashMap<Class<?>, List<Method>>();
		for (Method method : methods) {
			Class<?> cls = method.getDeclaringClass();
			if (!temp.containsKey(cls)) {
				temp.put(cls, new ArrayList<Method>());
			}
			List<Method> list = temp.get(cls);
			list.add(method);
		}

		for (Class<?> cls : temp.keySet()) {
			try {
				addMapping(cls, temp.get(cls));
			} catch (InstantiationException e) {
				logger.info("Error when instantiating " + cls.getName());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		extracters = new ArrayList<URLExtracter>(mapping.keySet());
	}

	private void addMapping(Class<?> cls, List<Method> methods) throws InstantiationException, IllegalAccessException {
		Object bean = cls.newInstance();
		for (Method method : methods) {
			// checking method is illegal or not
			boolean illegal = false;
			// checking parameter
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (!ConvertorFactory.support(parameterType)) {
					String logFormat = "%s.%s Parameter Not Support!";
					logger.info(String.format(logFormat, cls.getName(), method.getName()));
					illegal = true;
					break;
				}
			}
			if (illegal) {
				continue;
			}
			Route route = method.getAnnotation(Route.class);
			View view = new View(method, bean, route.methods());
			for (String path : route.value()) {
				URLExtracter extracter = new URLExtracter(path);
				if (!mapping.containsKey(extracter)) {
					mapping.put(extracter, new ArrayList<View>());
				}
				mapping.get(extracter).add(view);

				String logFormat = "\tRoute: %20s %10s to\t%15s.";
				logger.info(
						String.format(logFormat, path, StringUtils.join(route.methods(), ','), method.getName()));
			}
		}
	}

	private void scanInterceptors() {
		List<String> interceptorsPaths = new ArrayList<String>();
		interceptorsPaths.add(Config.getDefault(Config.INTERCEPTORS_PREFIX));
		if (!interceptorsPaths.contains(Config.get(Config.INTERCEPTORS_PREFIX)))
			interceptorsPaths.add(Config.get(Config.INTERCEPTORS_PREFIX));

		List<Interceptor> interceptorList = new ArrayList<Interceptor>();

		for (String path : interceptorsPaths) {
			Reflections reflections = new Reflections(path);
			Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Intercept.class);

			for (Class<?> cls : classes) {
				try {
					interceptorList.add((Interceptor) cls.newInstance());
					logger.info("Add interceptor: " + cls.getName());
				} catch (Exception e) {
					logger.info("Error when adding interceptor: " + cls.getName());
				}
			}
		}
		Collections.sort(interceptorList, new Comparator<Interceptor>() {
			@Override
			public int compare(Interceptor o1, Interceptor o2) {
				Intercept i1 = o1.getClass().getAnnotation(Intercept.class);
				Intercept i2 = o2.getClass().getAnnotation(Intercept.class);
				if (i1.value() == i2.value())
					return 0;
				else {
					return i1.value() < i2.value() ? -1 : 1;
				}
			}
		});
		interceptors = interceptorList.toArray(new Interceptor[0]);
	}

	public void dispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = req.getPathInfo();
		String method = req.getMethod();
		for (URLExtracter extracter : extracters) {
			if (extracter.isMatch(path)) {
				List<View> views = mapping.get(extracter);
				View targetView = null;
				for (View v : views) {
					if (v.isHttpMethodAllowed(method)) {
						targetView = v;
						break;
					}
				}

				// url match but method mismatch
				if (targetView == null) {
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					return;
				}

				// build request, response, cookies, session
				Request request = new Request(req);
				requests.set(request);
				Response response = new Response(resp);
				responses.set(response);
				Cookies cookies = new Cookies(req, resp);
				cookiez.set(cookies);

				
				// extract args from url
				String[] extractedArgs = extracter.extract(path);
				Class<?>[] argTypes = targetView.getArgTypes();
				Object[] args = ConvertorFactory.convertArray(argTypes, extractedArgs);

				// build interceptor chain
				InterceptorChain chain = new InterceptorChainImpl(interceptors, request, response, targetView, args);

				try {
					chain.doIntercept();
				} catch (Exception e) {
					e.printStackTrace();
				}
				resp.getWriter().flush();
				return;
			}
		}
		// mismatch all url, 404
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
