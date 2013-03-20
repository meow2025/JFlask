package in.keepp.jflask;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = -5681889829029448834L;
	
	private JFlask app;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		app = new JFlask();
		app.init(servletConfig);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String method = req.getMethod();
		
		if(method.equals(HttpMethod.GET) || method.equals(HttpMethod.POST)) {
			app.dispatch(req, resp);
		} else {
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

}
