package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.DbResult;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class CollectVisitor<T> implements DbVisitor<T>, DbResult<List<T>> {

	private final DbSource<T> source;

	private List<T> result;

	private Optional<Throwable> error;

	private boolean cancelled;

	/**
	 * Create new visitor instance from giver source.
	 *
	 * @param source
	 *            source
	 */
	public CollectVisitor(final DbSource<T> source) {
		this.source = source;

		this.result = new ArrayList<>();
		this.error = Optional.empty();
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		this.result.add(row);
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
	public List<T> get() throws ScriptExecutionException {
		this.source.accept(this);

		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.result;
	}

	@Override
	public String toString() {
		return "CollectVisitor [source=" + source + ", result=" + result + ", error=" + error + ", cancelled="
				+ cancelled + "]";
	}

}
