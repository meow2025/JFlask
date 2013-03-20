package in.keepp.jflask;

import in.keepp.jflask.exceptions.ArgumentMismatchException;
import in.keepp.jflask.exceptions.UnSupportedReturnTypeException;
import in.keepp.jflask.template.Template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

public class View {

	private Method method;
	private Object obj;
	private Class<?>[] argTypes;
	private String[] httpMethods;

	public View(Method method, Object obj, String[] httpMethods) {
		this.method = method;
		this.obj = obj;
		this.argTypes = method.getParameterTypes();
		this.httpMethods = httpMethods;
	}

	public Class<?>[] getArgTypes() {
		return argTypes;
	}

	public boolean isHttpMethodAllowed(String httpMethod) {
		for (String m : httpMethods) {
			if (m.equals(httpMethod))
				return true;
		}
		return false;
	}

	public void execute(Request request, Response response, Object... args) throws IOException,
			ArgumentMismatchException, UnSupportedReturnTypeException, InvocationTargetException {
		try {
			Object result = method.invoke(obj, args);

			if (result instanceof String) {
				response.append((CharSequence) result);
				response.setStatueCode(HttpServletResponse.SC_OK);
			} else if (result instanceof Template) {
				Template tpl = (Template) result;
				tpl.render(request, response);
			} else {
				throw new UnSupportedReturnTypeException(this.method);
			}
		} catch (IllegalArgumentException e) {
			throw new ArgumentMismatchException(this.method);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // should never happen
		}
	}
}
