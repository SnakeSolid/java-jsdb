package ru.snake.jsdb.db;

import java.sql.Connection;

import ru.snake.jsdb.db.mapper.DbMappers;
import ru.snake.jsdb.db.processor.DbQueryPublisher;

public final class DbCollection extends DbQueryPublisher {

	private DbQueryBuilder builder;

	public DbCollection(Connection connection, DbMappers mappers, String collectionName) {
		super(connection, mappers);

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
	protected String getQueryString() {
		return this.builder.build();
	}

	@Override
	public String toString() {
		return "DbCollection [builder=" + builder + "]";
	}

}
