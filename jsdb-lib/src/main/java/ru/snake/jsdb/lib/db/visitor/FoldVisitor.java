package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.BiFunction;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.DbResult;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

/**
 * Fold all elements from source using reducer function. Uses accumulator as
 * initial value. Returns final accumulator value as result.
 *
 * @author snake
 *
 * @param <T>
 *            source element type
 * @param <A>
 *            result type
 */
public final class FoldVisitor<T, A> implements DbVisitor<T>, DbResult<A> {

	private final DbSource<T> source;

	private final BiFunction<A, T, A> reducer;

	private A accumulator;

	private Optional<Throwable> error;

	private boolean cancelled;

	/**
	 * Create new visitor instance from giver source.
	 *
	 * @param source
	 *            source
	 * @param accumulator
	 *            accumulator
	 * @param reducer
	 *            reducer
	 */
	public FoldVisitor(final DbSource<T> source, final A accumulator, final BiFunction<A, T, A> reducer) {
		this.source = source;
		this.accumulator = accumulator;
		this.reducer = reducer;

		this.error = Optional.empty();
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		this.accumulator = this.reducer.apply(this.accumulator, row);
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
	public A get() throws ScriptExecutionException {
		this.source.accept(this);

		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.accumulator;
	}

	@Override
	public String toString() {
		return "FoldVisitor [source=" + source + ", reducer=" + reducer + ", accumulator=" + accumulator + ", error="
				+ error + ", cancelled=" + cancelled + "]";
	}

}
