package ru.snake.jsdb.error;

public class ScriptExecutionException extends JsDbException {

	private static final long serialVersionUID = 3417398103735889237L;

	public ScriptExecutionException() {
		super();
	}

	public ScriptExecutionException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return "Script execution failed";
	}

	@Override
	public String getMessage() {
		return "Script execution failed";
	}

}
