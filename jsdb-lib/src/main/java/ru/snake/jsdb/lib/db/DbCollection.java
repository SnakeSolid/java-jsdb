package ru.snake.jsdb.lib.db;

import java.sql.Connection;

import ru.snake.jsdb.lib.db.flow.AbstractDbFlow;
import ru.snake.jsdb.lib.db.mapper.DbMappers;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class DbCollection extends AbstractDbFlow<DbRow> {

	private final Connection connection;

	private final DbMappers mappers;

	private final DbQueryBuilder builder;

	/**
	 * Create new instance of database table source.
	 *
	 * @param connection
	 *            connection
	 * @param mappers
	 *            mappers
	 * @param collectionName
	 *            database table name
	 */
	public DbCollection(final Connection connection, final DbMappers mappers, final String collectionName) {
		this.connection = connection;
		this.mappers = mappers;
		this.builder = new DbQueryBuilder(collectionName);
	}

	public DbCollection project() {
		this.builder.setAllFields();

		return this;
	}

	public DbCollection project(String field) {
		this.builder.setFields(field);

		return this;
	}

	public DbCollection project(String[] fields) {
		this.builder.setFields(fields);

		return this;
	}

	public DbCollection where() {
		this.builder.setNoWhere();

		return this;
	}

	public DbCollection where(String where) {
		this.builder.setWhere(where);

		return this;
	}

	public DbCollection where(String[] conditions) {
		this.builder.setWhere(conditions);

		return this;
	}

	public DbCollection limit() {
		this.builder.setNoLimit();

		return this;
	}

	public DbCollection limit(int n) {
		this.builder.setLimit(n);

		return this;
	}

	@Override
	public void accept(final DbVisitor<DbRow> visitor) {
		DbSource<DbRow> source = new QueryDbSource(this.connection, this.mappers, this.builder.build());

		source.accept(visitor);
	}

	@Override
	public String toString() {
		return "DbCollection [connection=" + connection + ", mappers=" + mappers + ", builder=" + builder + "]";
	}

}
