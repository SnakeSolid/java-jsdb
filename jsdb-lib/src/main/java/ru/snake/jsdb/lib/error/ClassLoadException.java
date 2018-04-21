package ru.snake.jsdb.lib.error;

public class ClassLoadException extends JsDbException {

	private static final long serialVersionUID = -8321261050554714118L;

	public ClassLoadException() {
		super();
	}

	public ClassLoadException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Class load failed";
	}

	@Override
	public String getMessage() {
		return "Class load failed";
	}

}
