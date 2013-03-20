package in.keepp.jflask.interceptor;

import in.keepp.jflask.Request;
import in.keepp.jflask.Response;
import in.keepp.jflask.View;

public class InterceptorChainImpl implements InterceptorChain {

	private Interceptor[] interceptors;
	private int index;

	private Request request;
	private Response response;
	private View view;
	private Object[] args;

	public InterceptorChainImpl(Interceptor[] interceptors, Request request, Response response, View view, Object[] args) {
		this.interceptors = interceptors;
		this.index = 0;
		this.request = request;
		this.response = response;
		this.view = view;
		this.args = args;
	}

	@Override
	public void doIntercept() throws Exception {
		if (index < interceptors.length) {
			interceptors[index++].intercept(this);
		} else {
			view.execute(request, response, args);
		}
	}

	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}

	public View getView() {
		return view;
	}

	public Object[] getArgs() {
		return args;
	}

}
