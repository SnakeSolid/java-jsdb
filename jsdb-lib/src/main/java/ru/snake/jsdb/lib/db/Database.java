package ru.snake.jsdb.lib.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ru.snake.jsdb.lib.db.mapper.DbDummyMappers;
import ru.snake.jsdb.lib.db.mapper.DbMappers;
import ru.snake.jsdb.lib.error.InvalidOperationError;
import ru.snake.jsdb.lib.error.JsDbException;
import ru.snake.jsdb.lib.error.QueryExecutionException;

public class Database implements Map<String, Object> {

	private final Connection connection;

	private final Map<String, DbMappers> tableMappers;

	private final DbMappers tagMappers;

	public Database(Connection connection, Map<String, DbMappers> tableMappers, DbMappers tagMappers) {
		this.connection = connection;
		this.tableMappers = tableMappers;
		this.tagMappers = tagMappers;
	}

	public int execute(String sql) throws JsDbException {
		try (Statement statement = this.connection.createStatement()) {
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new QueryExecutionException(e);
		}
	}

	public DbQuery query(String sql) {
		return new DbQuery(this.connection, tagMappers, sql);
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public DbCollection get(Object key) {
		String collectionName = key.toString();
		DbMappers mappers = this.tableMappers.getOrDefault(collectionName, new DbDummyMappers());

		return new DbCollection(this.connection, mappers, collectionName);
	}

	@Override
	public Object put(String key, Object value) {
		throw new InvalidOperationError();
	}

	@Override
	public Object remove(Object key) {
		throw new InvalidOperationError();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new InvalidOperationError();
	}

	@Override
	public void clear() {
		throw new InvalidOperationError();
	}

	@Override
	public Set<String> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Collection<Object> values() {
		return Collections.emptySet();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return Collections.emptySet();
	}

	@Override
	public String toString() {
		return "Database [connection=" + connection + ", tableMappers=" + tableMappers + ", tagMappers=" + tagMappers
				+ "]";
	}

}
