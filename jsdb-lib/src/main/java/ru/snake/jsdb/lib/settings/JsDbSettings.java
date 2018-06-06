package ru.snake.jsdb.lib.settings;

import java.util.Collections;
import java.util.Set;

import ru.snake.jsdb.lib.Preconditions;
import ru.snake.jsdb.lib.error.InvalidDriverException;
import ru.snake.jsdb.lib.error.JsDbException;

/**
 * Container for JSDB library settings.
 *
 * @author snake
 *
 */
public class JsDbSettings {

	private String engineMimeType;

	private Set<String> libraryPaths;

	private String driver;

	private TableMapping tableFieldMappers;

	private TagMapping tagMapping;

	/**
	 * Create empty settings. Default engine type - JavaScript, default library
	 * path - file:lib/, all mappers are empty.
	 */
	public JsDbSettings() {
		this.engineMimeType = "application/javascript";
		this.libraryPaths = Collections.singleton("file:lib/");
		this.tableFieldMappers = new TableMapping();
		this.tagMapping = new TagMapping();
	}

	/**
	 * Validate settings. Throws {@link JsDbException} if some parameter has
	 * invalid value.
	 *
	 * @throws InvalidDriverException
	 *             if driver invalid
	 */
	public void validate() throws InvalidDriverException {
		if (this.driver == null) {
			throw new InvalidDriverException();
		}
	}

	/**
	 * Returns script engine MIME type.
	 *
	 * @return engine MIME type
	 */
	public String getEngineMimeType() {
		return engineMimeType;
	}

	/**
	 * Set script engine MIME type.
	 *
	 * @param engineMimeType
	 *            engine MIME type
	 */
	public void setEngineMimeType(String engineMimeType) {
		Preconditions.argumentNotNull(engineMimeType, "engineMimeType");
		Preconditions.argumentNotEmpty(engineMimeType, "engineMimeType");

		this.engineMimeType = engineMimeType;
	}

	public Set<String> getLibraryPaths() {
		return libraryPaths;
	}

	public void setLibraryPaths(Set<String> libraryPaths) {
		Preconditions.argumentNotNull(libraryPaths, "libraryPaths");
		Preconditions.argumentNotEmpty(libraryPaths, "libraryPaths");

		this.libraryPaths = libraryPaths;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		Preconditions.argumentNotNull(driver, "driver");

		this.driver = driver;
	}

	public TableMapping getTableFieldMapping() {
		return tableFieldMappers;
	}

	public void setTableFieldMappers(TableMapping tableFieldMappers) {
		Preconditions.argumentNotNull(tableFieldMappers, "tableFieldMappers");
		Preconditions.argument(!tableFieldMappers.isEmpty(), "Argument tableFieldMappers must not be empty");

		this.tableFieldMappers = tableFieldMappers;
	}

	public TagMapping getTagMapping() {
		return tagMapping;
	}

	public void setTagMapping(TagMapping tagMapping) {
		this.tagMapping = tagMapping;
	}

	@Override
	public String toString() {
		return "JsDbSettings [engineMimeType=" + engineMimeType + ", libraryPaths=" + libraryPaths + ", driver="
				+ driver + ", tableFieldMappers=" + tableFieldMappers + ", tagMapping=" + tagMapping + "]";
	}

}
