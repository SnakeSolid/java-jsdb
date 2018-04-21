package ru.snake.jsdb.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.snake.jsdb.db.mapper.DbDummyMappers;
import ru.snake.jsdb.db.mapper.DbMappers;
import ru.snake.jsdb.error.InvalidOperationError;
import ru.snake.jsdb.error.JsDbException;
import ru.snake.jsdb.error.QueryExecutionException;

public class Database implements Map<String, Object> {

	private static final Logger LOG = LoggerFactory.getLogger(Database.class);

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
		LOG.info("Database::size unimplemented");

		return 0;
	}

	@Override
	public boolean isEmpty() {
		LOG.info("Database::isEmpty unimplemented");

		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		LOG.info("Database::containsKey({}) unimplemented", key);

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		LOG.info("Database::containsValue({}) unimplemented", value);

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
		throw new InvalidOperationError();
	}

	@Override
	public Collection<Object> values() {
		throw new InvalidOperationError();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		throw new InvalidOperationError();
	}

	@Override
	public String toString() {
		return "Database [connection=" + connection + ", tableMappers=" + tableMappers + ", tagMappers=" + tagMappers
				+ "]";
	}

}
