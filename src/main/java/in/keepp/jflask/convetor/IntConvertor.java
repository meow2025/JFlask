package in.keepp.jflask.convetor;

public class IntConvertor implements Convetor<Integer> {

	@Override
	public Integer convert(String value) {
		return Integer.valueOf(value);
	}

}
