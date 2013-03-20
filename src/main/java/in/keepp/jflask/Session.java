package in.keepp.jflask;

import javax.servlet.http.HttpSession;

public class Session {
	private HttpSession httpSession;

	public Session(Request request) {
		this.httpSession = request.getSession();
	}

	public void set(String key, Object value) {
		httpSession.setAttribute(key, value);
	}

	public Object get(String key) {
		return httpSession.getAttribute(key);
	}
}
