package ru.snake.jsdb.lib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ru.snake.jsdb.lib.db.mapper.DbMappers;
import ru.snake.jsdb.lib.flow.DbSource;
import ru.snake.jsdb.lib.flow.DbVisitor;

public final class QueryDbSource implements DbSource<DbRow> {

	private final Connection connection;

	private final DbMappers mappers;

	private final String query;

	/**
	 * Create new instance of query source.
	 *
	 * @param connection
	 *            connection
	 * @param mappers
	 *            field mappers
	 * @param query
	 *            query string
	 */
	public QueryDbSource(final Connection connection, final DbMappers mappers, final String query) {
		this.connection = connection;
		this.mappers = mappers;
		this.query = query;
	}

	@Override
	public void accept(final DbVisitor<DbRow> visitor) {
		try (Statement statement = this.connection.createStatement();
				ResultSet resultSet = statement.executeQuery(this.query)) {
			ResultSetMetaData metadata = resultSet.getMetaData();
			int nColumns = metadata.getColumnCount();

			while (!visitor.isCancelled() && resultSet.next()) {
				DbRow row = new DbRow(2 * nColumns);

				for (int index = 1; index <= nColumns; index += 1) {
					String columnName = metadata.getColumnName(index);
					Object value = resultSet.getObject(index);

					if (resultSet.wasNull()) {
						row.put(columnName, null);
					} else {
						Object mapedValue = this.mappers.map(columnName, value);

						row.put(columnName, mapedValue);
					}
				}

				visitor.acceptNext(row);
			}

			visitor.complete();
		} catch (SQLException readResultException) {
			visitor.acceptError(readResultException);
		}
	}

	@Override
	public String toString() {
		return "QueryDbSource [connection=" + connection + ", mappers=" + mappers + ", query=" + query + "]";
	}

}
