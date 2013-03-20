package in.keepp.jflask.template;

import java.util.Map;

public abstract class TemplateFactory {

	private static TemplateFactory instance;

	public static void setTemplateFactory(TemplateFactory factory) {
		instance = factory;
	}

	public static TemplateFactory getInstance() {
		return instance;
	}

	public abstract void init();

	public abstract VelocityTemplate loadTemplate(String filename, Map<String, Object> model);

}
