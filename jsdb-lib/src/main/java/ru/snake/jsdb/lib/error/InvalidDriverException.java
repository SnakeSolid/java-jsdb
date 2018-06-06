package ru.snake.jsdb.lib.error;

/**
 * Exception represents invalid JDBC driver error in settings.
 *
 * @author snake
 *
 */
public class InvalidDriverException extends JsDbException {

	private static final long serialVersionUID = 723379045344422356L;

	public InvalidDriverException() {
		super();
	}

	public InvalidDriverException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Invalid JDBC driver";
	}

	@Override
	public String getMessage() {
		return "Invalid JDBC driver";
	}

}
