package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.Optional;

import ru.snake.jsdb.lib.flow.DbVisitor;

public final class TakeVisitor<T> implements DbVisitor<T> {

	private final DbVisitor<T> visitor;

	private final int count;

	private int n;

	private Optional<Throwable> error;

	private boolean cancelled;

	/**
	 * Create new visitor instance from giver source.
	 *
	 * @param visitor
	 *            visitor
	 * @param count
	 *            count
	 */
	public TakeVisitor(final DbVisitor<T> visitor, final int count) {
		this.visitor = visitor;
		this.count = count;

		this.n = 0;
		this.error = Optional.empty();
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		if (this.n < this.count) {
			this.visitor.acceptNext(row);
			this.n += 1;
		} else {
			this.cancelled = true;
		}
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
	public String toString() {
		return "TakeVisitor [visitor=" + visitor + ", count=" + count + ", n=" + n + ", error=" + error + ", cancelled="
				+ cancelled + "]";
	}

}
