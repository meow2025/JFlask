package in.keepp.jflask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLExtracter {

	private static Pattern intPattern = Pattern.compile("<int>");
	private static Pattern floatPattern = Pattern.compile("<float>");
	private static Pattern stringPattern = Pattern.compile("<str>");

	private static String intReplacement = "(\\\\d+)";
	private static String floatReplacement = "(\\\\d+\\\\.\\\\d+)";
	private static String stringReplacement = "(\\\\w+)";

	private Pattern pattern;
	private String url;

	public URLExtracter(String url) {
		this.url = url;
		String regex = convertToRegex(url);
		pattern = Pattern.compile(regex);
	}

	// convert url to regex
	private static String convertToRegex(String url) {
		url = intPattern.matcher(url).replaceAll(intReplacement);
		url = floatPattern.matcher(url).replaceAll(floatReplacement);
		url = stringPattern.matcher(url).replaceAll(stringReplacement);
		return url;
	}

	public boolean isMatch(String url) {
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public String[] extract(String url) {
		Matcher matcher = pattern.matcher(url);
		String[] result = null;
		if (matcher.find()) {
			result = new String[matcher.groupCount()];
			for (int i = 0; i < matcher.groupCount(); ++i) {
				result[i] = matcher.group(i + 1);
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.url.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof URLExtracter) {
			URLExtracter another = (URLExtracter) obj;
			if (url.equals(another.url))
				return true;
		}
		return false;
	}

}
