package in.keepp.jflask.interceptor;

import in.keepp.jflask.Request;
import in.keepp.jflask.Response;
import in.keepp.jflask.View;

public interface InterceptorChain {
	public void doIntercept() throws Exception;

	public Request getRequest();

	public Response getResponse();

	public View getView();

	public Object[] getArgs();
}
