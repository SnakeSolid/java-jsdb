package ru.snake.jsdb.lib.db.visitor;

import java.sql.SQLException;
import java.util.function.Function;

import ru.snake.jsdb.lib.flow.DbVisitor;

public final class MapVisitor<T, R> implements DbVisitor<T> {

	private final DbVisitor<R> visitor;

	private final Function<T, R> mapper;

	/**
	 * Create new mapper visitor instance from given source.
	 *
	 * @param visitor
	 *            visitor
	 * @param mapper
	 *            mapper function
	 */
	public MapVisitor(final DbVisitor<R> visitor, final Function<T, R> mapper) {
		this.visitor = visitor;
		this.mapper = mapper;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void acceptNext(final T row) {
		this.visitor.acceptNext(this.mapper.apply(row));
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
		return "MapVisitor [visitor=" + visitor + ", mapper=" + mapper + "]";
	}

}
