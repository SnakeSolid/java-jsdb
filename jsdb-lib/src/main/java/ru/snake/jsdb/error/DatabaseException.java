package ru.snake.jsdb.error;

public class DatabaseException extends JsDbException {

	private static final long serialVersionUID = 8985784441992778782L;

	public DatabaseException() {
		super();
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Database failed";
	}

	@Override
	public String getMessage() {
		return "Database failed";
	}

}
