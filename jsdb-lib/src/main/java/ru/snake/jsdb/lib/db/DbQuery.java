package ru.snake.jsdb.lib.db;

import java.sql.Connection;

import ru.snake.jsdb.lib.db.mapper.DbMappers;
import ru.snake.jsdb.lib.db.processor.DbQueryPublisher;

public final class DbQuery extends DbQueryPublisher {

	private String queryString;

	public DbQuery(Connection connection, DbMappers mappers, String queryString) {
		super(connection, mappers);

		this.queryString = queryString;
	}

	@Override
	protected String getQueryString() {
		return this.queryString;
	}

	@Override
	public String toString() {
		return "DbQuery [queryString=" + queryString + "]";
	}

}