package in.keepp.jflask.interceptor;

public interface Interceptor {
	public void intercept(InterceptorChain chain) throws Exception;
}
