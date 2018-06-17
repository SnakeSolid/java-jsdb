package ru.snake.jsdb.lib.db.flow;

import java.util.function.Predicate;

import ru.snake.jsdb.lib.db.visitor.FilterVisitor;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class FilterFlow<T> extends AbstractDbFlow<T> {

	private final DbSource<T> source;

	private final Predicate<T> filter;

	/**
	 * Create new flow from data source.
	 *
	 * @param source
	 *            source
	 * @param filter
	 *            filter
	 */
	public FilterFlow(final DbSource<T> source, final Predicate<T> filter) {
		this.source = source;
		this.filter = filter;
	}

	@Override
	public void accept(final DbVisitor<T> visitor) {
		this.source.accept(new FilterVisitor<>(visitor, filter));
	}

	@Override
	public String toString() {
		return "FilterFlow [source=" + source + ", filter=" + filter + "]";
	}

}
