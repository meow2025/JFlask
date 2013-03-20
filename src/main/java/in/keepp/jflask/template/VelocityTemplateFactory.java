package in.keepp.jflask.template;

import in.keepp.jflask.Config;

import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.velocity.app.Velocity;

public class VelocityTemplateFactory extends TemplateFactory {

	@Override
	public void init() {
		Properties p = new Properties();
		ServletContext context = Config.getServletConfig().getServletContext();
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, context.getRealPath(Config.get(Config.TEMPLATES_PATH)));
		Velocity.init(p);
	}

	@Override
	public VelocityTemplate loadTemplate(String filename, Map<String, Object> model) {
		org.apache.velocity.Template template = Velocity.getTemplate(filename);

		VelocityTemplate velocityTemplate = new VelocityTemplate(template);
		velocityTemplate.setModel(model);
		return velocityTemplate;
	}

}
