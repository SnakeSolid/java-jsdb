package ru.snake.jsdb.lib.db.flow;

import java.util.function.Function;

import ru.snake.jsdb.lib.db.visitor.MapVisitor;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class MapFlow<T, R> extends AbstractDbFlow<R> {

	private final DbSource<T> source;

	private final Function<T, R> mapper;

	/**
	 * Create new flow from data source.
	 *
	 * @param source
	 *            source
	 * @param mapper
	 *            mapper
	 */
	public MapFlow(final DbSource<T> source, final Function<T, R> mapper) {
		this.source = source;
		this.mapper = mapper;
	}

	@Override
	public void accept(final DbVisitor<R> visitor) {
		this.source.accept(new MapVisitor<>(visitor, mapper));
	}

	@Override
	public String toString() {
		return "MapFlow [source=" + source + ", mapper=" + mapper + "]";
	}

}
