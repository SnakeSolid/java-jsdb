package ru.snake.jsdb.cli.config;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Configuration {

	private Set<String> libraryPaths;

	private String jdbcDriver;

	private Map<String, Map<String, String>> fieldMappers;

	private Map<String, String> tagMappers;

	private String jdbcUrl;

	public Configuration() {
		this.libraryPaths = new TreeSet<String>();
		this.fieldMappers = new TreeMap<>();
		this.tagMappers = new TreeMap<>();
	}

	public void addLibraryPath(String libraryPath) {
		this.libraryPaths.add(libraryPath);
	}

	public void addFieldMapper(String tableName, String fieldName, String mapperClass) {
		this.fieldMappers.computeIfAbsent(tableName, e -> new TreeMap<>()).put(fieldName, mapperClass);
	}

	public void addTagMapper(String tag, String mapperClass) {
		this.tagMappers.put(tag, mapperClass);
	}

	public Set<String> getLibraryPaths() {
		return libraryPaths;
	}

	public void setLibraryPaths(Set<String> libraryPaths) {
		this.libraryPaths = libraryPaths;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public Map<String, Map<String, String>> getFieldMappers() {
		return fieldMappers;
	}

	public Map<String, String> getTagMappers() {
		return tagMappers;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	@Override
	public String toString() {
		return "Configuration [libraryPaths=" + libraryPaths + ", jdbcDriver=" + jdbcDriver + ", fieldMappers="
				+ fieldMappers + ", tagMappers=" + tagMappers + ", jdbcUrl=" + jdbcUrl + "]";
	}

}
