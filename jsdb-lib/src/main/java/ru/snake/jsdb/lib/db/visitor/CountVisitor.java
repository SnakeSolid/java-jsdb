package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.Optional;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.DbResult;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class CountVisitor<T> implements DbVisitor<T>, DbResult<Long> {

	private final DbSource<T> source;

	private long count;

	private Optional<Throwable> error;

	private boolean cancelled;

	/**
	 * Create new visitor instance from giver source.
	 *
	 * @param source
	 *            source
	 */
	public CountVisitor(final DbSource<T> source) {
		this.source = source;

		this.count = 0;
		this.error = Optional.empty();
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		this.count += 1;
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
	public Long get() throws ScriptExecutionException {
		this.source.accept(this);

		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.count;
	}

	@Override
	public String toString() {
		return "CountVisitor [source=" + source + ", count=" + count + ", error=" + error + ", cancelled=" + cancelled
				+ "]";
	}

}
