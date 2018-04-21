package ru.snake.jsdb.lib.error;

public class MalformedPathException extends JsDbException {

	private static final long serialVersionUID = -4128344581340575936L;

	public MalformedPathException() {
		super();
	}

	public MalformedPathException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Malformed path";
	}

	@Override
	public String getMessage() {
		return "Malformed path";
	}

}
