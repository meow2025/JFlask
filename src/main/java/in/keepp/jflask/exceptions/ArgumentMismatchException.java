package in.keepp.jflask.exceptions;

import java.lang.reflect.Method;

public class ArgumentMismatchException extends Exception {
	private static final long serialVersionUID = 4173194136907488605L;

	private String methodName;

	public ArgumentMismatchException(Method method) {
		this.methodName = method.getName();
	}

	@Override
	public String toString() {
		return "Argument mismatch on " + methodName;
	}

}
