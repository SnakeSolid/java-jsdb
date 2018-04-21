package ru.snake.jsdb.error;

public class JsDbError extends Error {

	private static final long serialVersionUID = -3299862551046680790L;

	public JsDbError() {
		super();
	}

	public JsDbError(String message) {
		super(message);
	}

	public JsDbError(String message, Throwable cause) {
		super(message, cause);
	}

	public JsDbError(Throwable cause) {
		super(cause);
	}

}
