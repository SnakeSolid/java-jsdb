package ru.snake.jsdb.lib.db.flow;

import ru.snake.jsdb.lib.db.visitor.SkipVisitor;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class SkipFlow<T> extends AbstractDbFlow<T> {

	private final DbSource<T> source;

	private final int count;

	/**
	 * Create new flow from data source.
	 *
	 * @param source
	 *            source
	 * @param count
	 *            count
	 */
	public SkipFlow(final DbSource<T> source, final int count) {
		this.source = source;
		this.count = count;
	}

	@Override
	public void accept(final DbVisitor<T> visitor) {
		this.source.accept(new SkipVisitor<>(visitor, this.count));
	}

	@Override
	public String toString() {
		return "SkipFlow [source=" + source + ", count=" + count + "]";
	}

}
