package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.function.Predicate;

import ru.snake.jsdb.lib.flow.DbVisitor;

public final class FilterVisitor<T> implements DbVisitor<T> {

	private final DbVisitor<T> visitor;

	private final Predicate<T> filter;

	private boolean cancelled;

	/**
	 * Create new filter visitor instance from given source.
	 *
	 * @param visitor
	 *            visitor
	 * @param filter
	 *            filter function
	 */
	public FilterVisitor(final DbVisitor<T> visitor, final Predicate<T> filter) {
		this.visitor = visitor;
		this.filter = filter;

		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void acceptNext(final T row) {
		if (this.filter.test(row)) {
			this.visitor.acceptNext(row);
		}
	}

	@Override
	public void acceptError(final SQLException exception) {
		this.visitor.acceptError(exception);
	}

	@Override
	public void complete() {
		this.visitor.complete();
	}

	@Override
	public String toString() {
		return "FilterVisitor [visitor=" + visitor + ", filter=" + filter + ", cancelled=" + cancelled + "]";
	}

}
