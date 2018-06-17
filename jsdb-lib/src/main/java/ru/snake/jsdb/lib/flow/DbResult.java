package ru.snake.jsdb.lib.flow;

import ru.snake.jsdb.lib.error.ScriptExecutionException;

@FunctionalInterface
public interface DbResult<T> {

	public T get() throws ScriptExecutionException;

}
