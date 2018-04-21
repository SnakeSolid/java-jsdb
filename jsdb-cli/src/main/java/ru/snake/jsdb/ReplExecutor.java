package ru.snake.jsdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import ru.snake.jsdb.error.ScriptExecutionException;

public final class ReplExecutor extends AbstractExecutor {

	private final JsDbContext context;

	private final InputStream in;

	public ReplExecutor(JsDbContext context, InputStream in, PrintStream out, PrintStream err) {
		super(out, err);

		this.context = context;
		this.in = in;
	}

	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.in))) {
			while (true) {
				this.out.print("> ");
				this.out.flush();

				String line = reader.readLine();

				if (line == null) {
					this.out.println();

					break;
				}

				try {
					showResult(context.execute(line));
				} catch (ScriptExecutionException e) {
					showError(e);
				}
			}
		} catch (IOException e) {
			showError(e);
		}
	}

	@Override
	public String toString() {
		return "ReplExecutor [context=" + context + ", in=" + in + "]";
	}

}
