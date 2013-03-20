package in.keepp.jflask.exceptions;

public class AbortException extends RuntimeException {
	private static final long serialVersionUID = 4240816003845045606L;

	private int statusCode;

	public AbortException(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
