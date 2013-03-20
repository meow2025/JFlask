package in.keepp.jflask.template;

import in.keepp.jflask.Request;
import in.keepp.jflask.Response;

import java.io.IOException;
import java.util.Map;

public interface Template {
	public void setModel(Map<String, Object> model);

	public void render(Request request, Response response) throws IOException;
}
