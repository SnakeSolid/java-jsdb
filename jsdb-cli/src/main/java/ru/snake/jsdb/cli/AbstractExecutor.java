package ru.snake.jsdb.cli;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractExecutor {

	protected final PrintStream out;

	protected final PrintStream err;

	public AbstractExecutor(PrintStream out, PrintStream err) {
		this.out = out;
		this.err = err;
	}

	public abstract void run();

	protected void showError(Throwable error) {
		Throwable cause = error;
		int level = 0;

		this.err.println("----< error >----");

		while (cause != null) {
			this.err.println(makeIdent(level) + cause.getLocalizedMessage());

			for (Throwable suppressed : error.getSuppressed()) {
				this.err.println(makeIdent(level) + "* " + suppressed.getLocalizedMessage());
			}

			cause = cause.getCause();
			level += 1;
		}
	}

	private String makeIdent(int level) {
		if (level == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder(2 * level);

		for (int i = 0; i < level - 1; i += 1) {
			builder.append("  ");
		}

		builder.append("> ");

		return builder.toString();
	}

	protected void showResult(Object result) {
		if (result == null) {
			this.out.println("---- null ----");
		} else {
			Class<? extends Object> resultClass = result.getClass();
			String className = resultClass.getSimpleName();

			this.out.println("----[ " + className + " ]----");

			if (result instanceof Iterable<?>) {
				Iterable<?> iterable = (Iterable<?>) result;

				iterable.forEach(this.out::println);
			} else if (result instanceof Iterator<?>) {
				Iterator<?> iterator = (Iterator<?>) result;

				iterator.forEachRemaining(this.out::println);
			} else if (result instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) result;

				map.forEach((k, v) -> this.out.println(k + " -> " + v));
			} else {
				this.out.println(result);
			}
		}
	}

}
