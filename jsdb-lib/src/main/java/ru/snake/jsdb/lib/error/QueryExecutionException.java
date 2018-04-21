package ru.snake.jsdb.lib.error;

public class QueryExecutionException extends JsDbException {

	private static final long serialVersionUID = 4851367878398283495L;

	public QueryExecutionException() {
		super();
	}

	public QueryExecutionException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Query execution failed";
	}

	@Override
	public String getMessage() {
		return "Query execution failed";
	}

}
