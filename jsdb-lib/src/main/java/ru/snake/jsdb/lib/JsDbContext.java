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

	public Object execute(String script) throws ScriptExecutionException {
		try {
			return engine.eval(script);
		} catch (ScriptException e) {
			throw new ScriptExecutionException(e);
		}
	}

	public Object execute(Reader reader) throws ScriptExecutionException {
		try {
			return engine.eval(reader);
		} catch (ScriptException e) {
			throw new ScriptExecutionException(e);
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
