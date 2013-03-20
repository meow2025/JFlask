package in.keepp.jflask;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

public class Response {

	private HttpServletResponse response;
	
	public Response(HttpServletResponse response) {
		this.response = response;
	}
	
	public void append(CharSequence csq) throws IOException {
		response.getWriter().append(csq);
	}

	public int getStatueCode() {
		return response.getStatus();
	}

	public void setStatueCode(int statueCode) {
		response.setStatus(statueCode);
	}

	public String getMimetype() {
		return response.getContentType();
	}

	public void setMimetype(String mimetype) {
		response.setContentType(mimetype);
	}

	public Writer getWriter() throws IOException {
		return response.getWriter();
	}

	public void sendError(int statueCode) throws IOException {
		response.sendError(statueCode);
	}
}
