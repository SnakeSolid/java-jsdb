package ru.snake.jsdb.lib.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DbQueryBuilder {

	private final String tableName;

	private List<String> fields;

	private Optional<String> where;

	private Optional<Integer> limit;

	public DbQueryBuilder(String tableName) {
		this.tableName = tableName;

		this.fields = Collections.emptyList();
		this.where = Optional.empty();
		this.limit = Optional.empty();
	}

	public String build() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");

		if (this.fields.isEmpty()) {
			builder.append("*");
		} else {
			String fieldString = fields.stream().collect(Collectors.joining(", "));

			builder.append(fieldString);
		}

		builder.append(" FROM ");
		builder.append(this.tableName);

		this.where.ifPresent(where -> {
			builder.append(" WHERE ");
			builder.append(where);
		});

		this.limit.ifPresent(limit -> {
			builder.append(" LIMIT ");
			builder.append(limit);
		});

		return builder.toString();
	}

	public void setAllFields() {
		this.fields = Collections.emptyList();
	}

	public void setFields(String... fields) {
		this.fields = Arrays.asList(fields);
	}

	public void setNoWhere() {
		this.limit = Optional.empty();
	}

	public void setWhere(String where) {
		this.where = Optional.of(where);
	}

	public void setWhere(String... conditions) {
		List<String> conditionList = Arrays.asList(conditions);
		String whereString = conditionList.stream().collect(Collectors.joining(", "));

		this.where = Optional.of(whereString);
	}

	public void setNoLimit() {
		this.limit = Optional.empty();
	}

	public void setLimit(int value) {
		this.limit = Optional.of(value);
	}

	@Override
	public String toString() {
		return "DbQueryBuilder [tableName=" + tableName + ", fields=" + fields + ", where=" + where + ", limit=" + limit
				+ "]";
	}

}
