package in.keepp.jflask.template;

import in.keepp.jflask.Request;
import in.keepp.jflask.Response;

import java.io.IOException;
import java.util.Map;

import org.apache.velocity.VelocityContext;

public class VelocityTemplate implements Template {

	private org.apache.velocity.Template template;
	private VelocityContext context;

	public VelocityTemplate(org.apache.velocity.Template template) {
		this.template = template;
	}

	@Override
	public void render(Request request, Response response) throws IOException {
		template.merge(context, response.getWriter());
	}

	@Override
	public void setModel(Map<String, Object> model) {
		context = new VelocityContext(model);
	}

}
