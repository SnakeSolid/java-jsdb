package ru.snake.jsdb.lib.error;

public class InvalidOperationError extends JsDbError {

	private static final long serialVersionUID = -825291479744106397L;

	@Override
	public String getLocalizedMessage() {
		return "Invalid operation";
	}

	@Override
	public String getMessage() {
		return "Invalid operation";
	}

}
