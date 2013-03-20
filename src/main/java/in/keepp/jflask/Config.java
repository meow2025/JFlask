package in.keepp.jflask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	public static String VIEWS_PREFIX = "VIEWS_PREFIX";
	public static String INTERCEPTORS_PREFIX = "INTERCEPTORS_PREFIX";
	public static String TEMPLATES_PATH = "TEMPLATES_PATH";

	private static Properties defaultProperties;
	private static Properties properties;
	private static ServletConfig servletConfig;
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	public static void load(ServletConfig servletConfig) throws IOException {
		Config.servletConfig = servletConfig;
		
		ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = resourceLoader.getResourceAsStream("default.properties");
		defaultProperties = new Properties();
		defaultProperties.load(inputStream);
		inputStream.close();
		try {
			inputStream = resourceLoader.getResourceAsStream("app.properties");
			properties = new Properties();
			properties.load(inputStream);
		} catch (Exception e) {
			// user havn't define any properties
		}
		
		logger.info("Views prefix: " + get(VIEWS_PREFIX));
		logger.info("Interceptors prefix: " + get(INTERCEPTORS_PREFIX));
		logger.info("Templates path: " + get(TEMPLATES_PATH));
	}

	public static String get(String key) {
		if (properties != null && properties.getProperty(key) != null) {
			return properties.getProperty(key);
		}
		return getDefault(key);
	}

	public static String getDefault(String key) {
		return defaultProperties.getProperty(key);
	}

	public static ServletConfig getServletConfig() {
		return servletConfig;
	}
}
