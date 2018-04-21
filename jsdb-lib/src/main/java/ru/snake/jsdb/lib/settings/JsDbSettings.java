package ru.snake.jsdb.lib.settings;

import java.util.Collections;
import java.util.Set;

import ru.snake.jsdb.lib.Preconditions;

public class JsDbSettings {

	private String engineMimeType;

	private Set<String> libraryPaths;

	private Set<String> drivers;

	private TableMapping tableFieldMappers;

	private TagMapping tagMapping;

	public JsDbSettings() {
		this.engineMimeType = "application/javascript";
		this.libraryPaths = Collections.singleton("file:lib/");
		this.drivers = Collections.emptySet();
		this.tableFieldMappers = new TableMapping();
		this.tagMapping = new TagMapping();
	}

	public void validate() {
		// TODO: complete method
	}

	public String getEngineMimeType() {
		return engineMimeType;
	}

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

	public Set<String> getDrivers() {
		return drivers;
	}

	public void setDrivers(Set<String> drivers) {
		Preconditions.argumentNotNull(drivers, "drivers");
		Preconditions.argumentNotEmpty(drivers, "drivers");

		this.drivers = drivers;
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
		return "JsDbSettings [engineMimeType=" + engineMimeType + ", libraryPaths=" + libraryPaths + ", drivers="
				+ drivers + ", tableFieldMappers=" + tableFieldMappers + ", tagMapping=" + tagMapping + "]";
	}

}
