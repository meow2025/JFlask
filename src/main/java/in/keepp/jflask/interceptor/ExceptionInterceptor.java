package in.keepp.jflask.interceptor;

import in.keepp.jflask.Response;
import in.keepp.jflask.annotations.Intercept;
import in.keepp.jflask.exceptions.AbortException;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

@Intercept(Integer.MIN_VALUE)
public class ExceptionInterceptor implements Interceptor {

	@Override
	public void intercept(InterceptorChain chain) throws Exception {
		try {
			chain.doIntercept();
		} catch (InvocationTargetException e) {
			handleInvocationTargetException(chain, e);
		} catch (Exception e) {
			e.printStackTrace((PrintWriter) chain.getResponse().getWriter());
		}
	}

	private void handleInvocationTargetException(InterceptorChain chain, InvocationTargetException e)
			throws IOException {
		if (e.getCause() instanceof AbortException) {
			Response response = chain.getResponse();
			response.sendError(((AbortException) e.getCause()).getStatusCode());
		} else {
			e.printStackTrace((PrintWriter) chain.getResponse().getWriter());
		}
	}

}
