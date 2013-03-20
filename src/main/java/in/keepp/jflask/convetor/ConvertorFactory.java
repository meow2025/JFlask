package in.keepp.jflask.convetor;

import java.util.HashMap;
import java.util.Map;

public class ConvertorFactory {
	private static Map<Class<?>, Convetor<?>> mapping = new HashMap<Class<?>, Convetor<?>>();

	static {
		mapping.put(Integer.class, new IntConvertor());
		mapping.put(int.class, new IntConvertor());
		mapping.put(Float.class, new FloatConvertor());
		mapping.put(float.class, new FloatConvertor());
		mapping.put(String.class, new StringConvertor());
	}

	public static boolean support(Class<?> targetClass) {
		return mapping.containsKey(targetClass);
	}

	public static Object convert(Class<?> targetClass, String value) {
		return mapping.get(targetClass).convert(value);
	}

	public static Object[] convertArray(Class<?>[] targetClasses, String[] values) {
		Object[] result = new Object[values.length];
		for (int i = 0; i < values.length; ++i)
			result[i] = convert(targetClasses[i], values[i]);
		return result;
	}
}
