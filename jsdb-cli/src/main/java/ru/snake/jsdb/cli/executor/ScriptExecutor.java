package ru.snake.jsdb.cli.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import ru.snake.jsdb.lib.JsDbContext;
import ru.snake.jsdb.lib.error.ScriptExecutionException;

public final class ScriptExecutor extends AbstractExecutor {

	private final JsDbContext context;

	private final File script;

	public ScriptExecutor(JsDbContext context, File script, PrintStream out, PrintStream err) {
		super(out, err);

		this.context = context;
		this.script = script;
	}

	@Override
	public void run() {
		try (FileReader reader = new FileReader(script)) {
			try {
				showResult(context.execute(reader));
			} catch (ScriptExecutionException e) {
				showError(e);
			}
		} catch (FileNotFoundException e) {
			showError(e);
		} catch (IOException e) {
			showError(e);
		}
	}

	@Override
	public String toString() {
		return "ScriptExecutor [context=" + context + ", script=" + script + "]";
	}

}
