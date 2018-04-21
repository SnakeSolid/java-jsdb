package ru.snake.jsdb.db.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ru.snake.jsdb.db.DbRow;
import ru.snake.jsdb.db.mapper.DbMappers;

public abstract class DbQueryPublisher extends DbPublisher<DbRow> {

	private final Connection connection;

	private final DbMappers mappers;

	public DbQueryPublisher(Connection connection, DbMappers mappers) {
		this.connection = connection;
		this.mappers = mappers;
	}

	@Override
	public final void consume() {
		String queryString = getQueryString();

		try (Statement statement = this.connection.createStatement();
				ResultSet resultSet = statement.executeQuery(queryString)) {
			ResultSetMetaData metadata = resultSet.getMetaData();
			int nColumns = metadata.getColumnCount();

			while (!this.cancelled && resultSet.next()) {
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

				fireNext(row);
			}

			fireComplete();
		} catch (SQLException readResultException) {
			fireError(readResultException);
		}
	}

	protected abstract String getQueryString();

	@Override
	public String toString() {
		return "DbQueryPublisher [connection=" + connection + "]";
	}

}
