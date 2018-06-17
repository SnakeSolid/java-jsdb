package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.DbResult;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class EachVisitor<T> implements DbVisitor<T>, DbResult<Void> {

	private final DbSource<T> source;

	private final Consumer<T> consumer;

	private Optional<Throwable> error;

	private boolean cancelled;

	/**
	 * Create new visitor instance from giver source.
	 *
	 * @param source
	 *            source
	 * @param consumer
	 *            consumer
	 */
	public EachVisitor(final DbSource<T> source, final Consumer<T> consumer) {
		this.source = source;
		this.consumer = consumer;

		this.error = Optional.empty();
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		this.consumer.accept(row);
	}

	@Override
	public void acceptError(final SQLException exception) {
		this.error = Optional.of(exception);
		this.cancelled = true;
	}

	@Override
	public void complete() {
	}

	@Override
	public Void get() throws ScriptExecutionException {
		this.source.accept(this);

		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return null;
	}

	@Override
	public String toString() {
		return "EachVisitor [source=" + source + ", consumer=" + consumer + ", error=" + error + ", cancelled="
				+ cancelled + "]";
	}

}
