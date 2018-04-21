package ru.snake.jsdb.error;

public class JsDbException extends Exception {

	private static final long serialVersionUID = -1010270840898305189L;

	public JsDbException() {
		super();
	}

	public JsDbException(String message) {
		super(message);
	}

	public JsDbException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsDbException(Throwable cause) {
		super(cause);
	}

}
