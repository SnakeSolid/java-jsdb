package ru.snake.jsdb.lib.flow;

import java.sql.SQLException;

public interface DbVisitor<T> {

	public boolean isCancelled();

	public void acceptNext(T row);

	public void acceptError(SQLException exception);

	public void complete();

}
