package ru.snake.jsdb.cli.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Arguments {

	private List<String> libraryPaths;

	private Map<String, String> fieldMappers;

	private Map<String, String> tagMappers;

	private List<String> scriptPaths;

	private String configPath;

	private String jdbcDriver;

	private String jdbcUrl;

	private boolean noRepl;

	public Arguments() {
		this.libraryPaths = new ArrayList<>();
		this.fieldMappers = new TreeMap<>();
		this.tagMappers = new TreeMap<>();
		this.scriptPaths = new ArrayList<>();
	}

	public void addLibraryPath(String libraryPath) {
		this.libraryPaths.add(libraryPath);
	}

	public void addFieldMapper(String tableField, String mapperClass) {
		this.fieldMappers.put(tableField, mapperClass);
	}

	public void addTagMapper(String tag, String mapperClass) {
		this.tagMappers.put(tag, mapperClass);
	}

	public void addScriptPath(String scriptPath) {
		this.scriptPaths.add(scriptPath);
	}

	public List<String> getLibraryPaths() {
		return libraryPaths;
	}

	public Map<String, String> getFieldMappers() {
		return fieldMappers;
	}

	public Map<String, String> getTagMappers() {
		return tagMappers;
	}

	public List<String> getScriptPaths() {
		return scriptPaths;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public boolean isNoRepl() {
		return noRepl;
	}

	public void setNoRepl(boolean noRepl) {
		this.noRepl = noRepl;
	}

	@Override
	public String toString() {
		return "Arguments [libraryPaths=" + libraryPaths + ", fieldMappers=" + fieldMappers + ", tagMappers="
				+ tagMappers + ", scriptPaths=" + scriptPaths + ", configPath=" + configPath + ", jdbcDriver="
				+ jdbcDriver + ", jdbcUrl=" + jdbcUrl + ", noRepl=" + noRepl + "]";
	}

}
