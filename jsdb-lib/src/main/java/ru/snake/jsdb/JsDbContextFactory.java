package ru.snake.jsdb;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import ru.snake.jsdb.db.Database;
import ru.snake.jsdb.db.mapper.DbFieldMappers;
import ru.snake.jsdb.db.mapper.DbMapper;
import ru.snake.jsdb.db.mapper.DbMappers;
import ru.snake.jsdb.db.mapper.DbTagMappers;
import ru.snake.jsdb.error.DatabaseException;
import ru.snake.jsdb.error.ClassLoadException;
import ru.snake.jsdb.error.JsDbException;
import ru.snake.jsdb.error.MalformedPathException;
import ru.snake.jsdb.settings.FieldMapping;
import ru.snake.jsdb.settings.JsDbSettings;
import ru.snake.jsdb.settings.TableMapping;
import ru.snake.jsdb.settings.TagMapping;
import ru.snake.jsdb.wrapper.DriverWrapper;

public class JsDbContextFactory {

	private JsDbContextFactory() {
	}

	public static JsDbContext create(JsDbSettings settings, String connectionString) throws JsDbException {
		settings.validate();

		Set<String> libraryPaths = settings.getLibraryPaths();

		try {
			URLClassLoader classLoader = createClassLoader(libraryPaths);

			return createContext(settings, connectionString, classLoader);
		} catch (MalformedURLException e) {
			throw new MalformedPathException(e);
		}
	}

	private static JsDbContext createContext(JsDbSettings settings, String connectionString, URLClassLoader classLoader)
			throws DatabaseException, ClassLoadException {
		Set<String> drivers = settings.getDrivers();

		try {
			Map<String, DbMappers> tableMappers = loadTableMappers(classLoader, settings);
			DbMappers tagMappers = loadTagMappers(classLoader, settings);

			loadJdbcDrivers(classLoader, drivers);

			Connection connection = createConnection(connectionString, classLoader);
			String engineMimeType = settings.getEngineMimeType();
			ScriptEngineManager engineManager = new ScriptEngineManager();
			ScriptEngine engine = engineManager.getEngineByMimeType(engineMimeType);
			Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
			Database database = new Database(connection, tableMappers, tagMappers);
			bindings.put("db", database);

			return new JsDbContext(classLoader, connection, engine);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			ClassLoadException exception = new ClassLoadException(e);
			safeClose(classLoader, exception);

			throw exception;
		}
	}

	private static DbTagMappers loadTagMappers(URLClassLoader classLoader, JsDbSettings settings)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		DbTagMappers tagMappers = new DbTagMappers();
		TagMapping tagMapping = settings.getTagMapping();

		for (String tagName : tagMapping.tagNames()) {
			String className = tagMapping.get(tagName);
			Class<?> mapperClass = classLoader.loadClass(className);

			if (mapperClass.isAssignableFrom(DbMapper.class)) {
				Object instance = mapperClass.newInstance();

				tagMappers.insert(tagName, (DbMapper) instance);
			}
		}

		return tagMappers;
	}

	private static Map<String, DbMappers> loadTableMappers(URLClassLoader classLoader, JsDbSettings settings)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Map<String, DbMappers> tableMappers = new HashMap<>();
		TableMapping tableMapping = settings.getTableFieldMapping();

		for (String tableName : tableMapping.tableNames()) {
			FieldMapping fieldMapping = tableMapping.get(tableName);
			DbFieldMappers fieldMappers = new DbFieldMappers();

			for (String fieldName : fieldMapping.fieldNames()) {
				String className = fieldMapping.get(fieldName);
				Class<?> mapperClass = classLoader.loadClass(className);

				if (DbMapper.class.isAssignableFrom(mapperClass)) {
					Object instance = mapperClass.newInstance();

					fieldMappers.insert(fieldName, (DbMapper) instance);
				}
			}

			tableMappers.put(tableName, fieldMappers);
		}

		return tableMappers;
	}

	private static Connection createConnection(String connectionString, URLClassLoader classLoader)
			throws DatabaseException {
		try {
			return DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			DatabaseException exception = new DatabaseException(e);
			safeClose(classLoader, exception);

			throw exception;
		}
	}

	private static JsDbException safeClose(Closeable closable, JsDbException exception) {
		try {
			closable.close();
		} catch (IOException e) {
			exception.addSuppressed(e);
		}

		return exception;
	}

	private static void loadJdbcDrivers(URLClassLoader classLoader, Set<String> drivers)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		for (String className : drivers) {
			Class<?> driverClass = classLoader.loadClass(className);

			if (Driver.class.isAssignableFrom(driverClass)) {
				Driver driver = (Driver) driverClass.newInstance();
				DriverWrapper wrapper = new DriverWrapper(driver);

				DriverManager.registerDriver(wrapper);
			}
		}
	}

	/**
	 * Create new {@link URLClassLoader} for dynamic loading field mappers and JDBC
	 * drivers.
	 *
	 * @param libraryPaths
	 *            library paths
	 * @return class loader
	 * @throws MalformedURLException
	 */
	private static URLClassLoader createClassLoader(Set<String> libraryPaths) throws MalformedURLException {
		URL[] urls = new URL[libraryPaths.size()];
		Iterator<String> it = libraryPaths.iterator();
		int index = 0;

		while (it.hasNext()) {
			String path = it.next();
			urls[index] = new URL(path);

			index += 1;
		}

		return new URLClassLoader(urls);
	}

}
