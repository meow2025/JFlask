package in.keepp.jflask.convetor;

public class FloatConvertor implements Convetor<Float> {

	@Override
	public Float convert(String value) {
		return Float.valueOf(value);
	}

}
