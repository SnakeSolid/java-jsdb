package ru.snake.jsdb.lib.db;

import java.sql.Connection;

import ru.snake.jsdb.lib.db.flow.AbstractDbFlow;
import ru.snake.jsdb.lib.db.mapper.DbMappers;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class DbQuery extends AbstractDbFlow<DbRow> {

	private final Connection connection;

	private final DbMappers mappers;

	private final String queryString;

	/**
	 * Create new instance of query source.
	 *
	 * @param connection
	 *            connection
	 * @param mappers
	 *            mappers
	 * @param queryString
	 *            query string
	 */
	public DbQuery(final Connection connection, final DbMappers mappers, final String queryString) {
		this.connection = connection;
		this.mappers = mappers;
		this.queryString = queryString;
	}

	@Override
	public void accept(final DbVisitor<DbRow> visitor) {
		DbSource<DbRow> source = new QueryDbSource(this.connection, this.mappers, this.queryString);

		source.accept(visitor);
	}

	@Override
	public String toString() {
		return "DbQuery [connection=" + connection + ", mappers=" + mappers + ", queryString=" + queryString + "]";
	}

}
