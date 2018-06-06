package ru.snake.jsdb.lib;

import java.io.IOException;
import java.io.Reader;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import ru.snake.jsdb.lib.error.DatabaseException;
import ru.snake.jsdb.lib.error.JsDbException;
import ru.snake.jsdb.lib.error.ScriptExecutionException;

public final class JsDbContext implements AutoCloseable {

	private final URLClassLoader classLoader;

	private final Connection connection;

	private final ScriptEngine engine;

	public JsDbContext(URLClassLoader classLoader, Connection connection, ScriptEngine engine) {
		this.classLoader = classLoader;
		this.connection = connection;
		this.engine = engine;
	}

	/**
	 * Execute the specified script from string value. Return script execution
	 * result.
	 *
	 * @param script
	 *            script
	 * @return execution result
	 * @throws ScriptExecutionException
	 *             if runtime or script error occurred
	 */
	public Object execute(final String script) throws ScriptExecutionException {
		try {
			return engine.eval(script);
		} catch (ScriptException e) {
			throw new ScriptExecutionException(e);
		} catch (RuntimeException e) {
			throw new ScriptExecutionException(e);
		}
	}

	/**
	 * Execute the specified script from reader. Return script execution result.
	 *
	 * @param reader
	 *            reader
	 * @return execution result
	 * @throws ScriptExecutionException
	 *             if runtime or script error occurred
	 */
	public Object execute(final Reader reader) throws ScriptExecutionException {
		try {
			return engine.eval(reader);
		} catch (ScriptException e) {
			throw new ScriptExecutionException(e);
		} catch (RuntimeException e) {
			Throwable cause = e.getCause();

			if (cause != null && cause instanceof ScriptExecutionException) {
				throw (ScriptExecutionException) cause;
			}

			throw e;
		}
	}

	@Override
	public void close() throws JsDbException {
		JsDbException exception = null;

		try {
			connection.close();
		} catch (SQLException e) {
			exception = new DatabaseException(e);
		}

		try {
			classLoader.close();
		} catch (IOException e) {
			if (exception == null) {
				exception = new JsDbException(e);
			} else {
				exception.addSuppressed(e);
			}
		}

		if (exception != null) {
			throw exception;
		}
	}

	@Override
	public String toString() {
		return "JsDbContext [classLoader=" + classLoader + ", connection=" + connection + ", engine=" + engine + "]";
	}

}
