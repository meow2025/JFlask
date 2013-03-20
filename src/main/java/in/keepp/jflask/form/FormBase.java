package in.keepp.jflask.form;

import in.keepp.jflask.Request;
import in.keepp.jflask.annotations.Input;
import in.keepp.jflask.convetor.ConvertorFactory;

import java.lang.reflect.Field;

public class FormBase<T> {
	@SuppressWarnings("unchecked")
	public T fill(Request request) {
		Field[] fields = getClass().getDeclaredFields();
		if (fields == null)
			return (T) this; // do nothing

		for (Field field : fields) {
			Input input = field.getAnnotation(Input.class);
			if (input == null)
				continue;
			String name = input.value().length() != 0 ? input.value() : field.getName();
			String value = request.getArg(name);
			if (value != null) {
				try {
					Object obj = ConvertorFactory.convert(field.getType(), value);
					field.set(this, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return (T) this;
	}

}
