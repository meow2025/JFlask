package in.keepp.jflask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Request {

	private String method;
	private String url;
	private String path;
	private HttpServletRequest httpServletRequest;
	
	public Request(HttpServletRequest request) {
		this.httpServletRequest = request;
		this.method = request.getMethod();
		this.url = request.getRequestURL().toString();
		this.path = request.getPathInfo();

	}
	
	public String getArg(String name) {
		return httpServletRequest.getParameter(name);
	}
	
	public String[] getArgs(String name) {
		return httpServletRequest.getParameterValues(name);
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	
	public HttpSession getSession() {
		return httpServletRequest.getSession(true);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
