package in.keepp.jflask.exceptions;

import java.lang.reflect.Method;

public class UnSupportedReturnTypeException extends Exception {
	private static final long serialVersionUID = 789066635789171536L;

	private String methodName;

	public UnSupportedReturnTypeException(Method method) {
		this.methodName = method.getName();
	}

	@Override
	public String toString() {
		return "Return type of " + methodName + "is not supported.";
	}
}
