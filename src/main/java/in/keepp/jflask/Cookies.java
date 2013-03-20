package in.keepp.jflask;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
	private HttpServletResponse response;
	private Map<String, String> cookies = new HashMap<String, String>();

	public Cookies(HttpServletRequest request, HttpServletResponse response) {
		this.response = response;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				this.cookies.put(cookie.getName(), cookie.getValue());
			}
		}
	}

	public String get(String key) {
		return cookies.get(key);
	}

	public void set(String key, String value) {
		response.addCookie(new Cookie(key, value));
	}
}
