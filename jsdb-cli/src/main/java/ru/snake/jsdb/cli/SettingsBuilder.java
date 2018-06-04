package ru.snake.jsdb.cli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.snake.jsdb.cli.args.Arguments;
import ru.snake.jsdb.cli.config.Configuration;
import ru.snake.jsdb.lib.settings.FieldMapping;
import ru.snake.jsdb.lib.settings.JsDbSettings;
import ru.snake.jsdb.lib.settings.TableMapping;
import ru.snake.jsdb.lib.settings.TagMapping;

public final class SettingsBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SettingsBuilder.class);

	private final Set<String> libraryPaths;

	private final Map<String, Map<String, String>> fieldMappers;

	private final Map<String, String> tagMappers;

	private Set<String> jdbcDrivers;

	private String jdbcUrl;

	/**
	 * Create empty builder instance. Initially library paths is empty, all
	 * mappers is empty, drivers is empty and connection string is {@code null}.
	 */
	public SettingsBuilder() {
		this.libraryPaths = new HashSet<>();
		this.fieldMappers = new HashMap<>();
		this.tagMappers = new HashMap<>();
		this.jdbcDrivers = new HashSet<>();
	}

	/**
	 * Update internal builder state with given CLI arguments. Library paths,
	 * data mappers and drivers will be extended with configuration. Connection
	 * string will be replaced with configured JDBC URL.
	 *
	 * @param arguments
	 *            CLI arguments
	 * @return current builder instance
	 */
	public SettingsBuilder withArguments(final Arguments arguments) {
		Map<String, String> argFieldMappers = arguments.getFieldMappers();
		String argsDriver = arguments.getJdbcDriver();
		String argsUrl = arguments.getJdbcUrl();

		this.libraryPaths.addAll(arguments.getLibraryPaths());

		argFieldMappers.forEach((key, value) -> {
			int dotPosition = key.lastIndexOf('.');

			if (dotPosition != -1 && dotPosition > 0 && dotPosition < key.length()) {
				String tableName = key.substring(0, dotPosition);
				String fieldName = key.substring(dotPosition + 1);

				this.fieldMappers.computeIfAbsent(tableName, k -> new TreeMap<>()).put(fieldName, value);
			} else {
				LOG.warn("Incorrect table filed definition in field mapping: `{}`", key);
			}
		});

		this.tagMappers.putAll(arguments.getTagMappers());

		if (argsDriver != null) {
			this.jdbcDrivers.add(argsDriver);
		}

		if (argsUrl != null) {
			this.jdbcUrl = argsUrl;
		}

		return this;
	}

	/**
	 * Update internal builder state with given configuration parameters.
	 * Library paths, data mappers and drivers will be extended with
	 * configuration. Connection string will be replaced with configured JDBC
	 * URL.
	 *
	 * @param config
	 *            CLI configuration
	 * @return current builder instance
	 */
	public SettingsBuilder withConfiguration(final Configuration config) {
		Map<String, Map<String, String>> confFieldMappers = config.getFieldMappers();
		String confogDriver = config.getJdbcDriver();
		String configUrl = config.getJdbcUrl();

		this.libraryPaths.addAll(config.getLibraryPaths());

		confFieldMappers.forEach((tableName,
				fields) -> this.fieldMappers.computeIfAbsent(tableName, e -> new TreeMap<>()).putAll(fields));

		this.tagMappers.putAll(config.getTagMappers());

		if (confogDriver != null) {
			this.jdbcDrivers.add(confogDriver);
		}

		if (configUrl != null) {
			this.jdbcUrl = configUrl;
		}

		return this;
	}

	public boolean validate() {
		boolean valid = true;

		if (this.libraryPaths.isEmpty()) {
			LOG.error("Library paths is empty");

			valid = false;
		}

		if (this.jdbcDrivers.isEmpty()) {
			LOG.error("JDBC drivers is empty");

			valid = false;
		}

		if (this.jdbcUrl == null) {
			LOG.error("JDBC URL is empty");

			valid = false;
		}

		return valid;
	}

	public JsDbSettings build() {
		JsDbSettings settings = new JsDbSettings();

		settings.setLibraryPaths(this.libraryPaths);
		settings.setDrivers(this.jdbcDrivers);

		putFieldMappers(settings);
		putTagMappers(settings);

		return settings;
	}

	/**
	 * Returns connection string URL if it's present otherwise returns
	 * {@code null}.
	 *
	 * @return JDBC connection URL
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	private void putTagMappers(JsDbSettings settings) {
		TagMapping tagMapping = settings.getTagMapping();

		this.tagMappers.forEach((key, value) -> {
			tagMapping.insert(key.toString(), value.toString());
		});
	}

	private void putFieldMappers(JsDbSettings settings) {
		TableMapping tableMapping = settings.getTableFieldMapping();

		this.fieldMappers.forEach((tableName, fields) -> {
			if (tableMapping.contains(tableName)) {
				FieldMapping fieldMapping = tableMapping.get(tableName);

				fields.forEach(fieldMapping::insert);
			} else {
				FieldMapping fieldMapping = new FieldMapping();

				fields.forEach(fieldMapping::insert);
				tableMapping.insert(tableName, fieldMapping);
			}
		});
	}

	@Override
	public String toString() {
		return "SettingsBuilder [libraryPaths=" + libraryPaths + ", fieldMappers=" + fieldMappers + ", tagMappers="
				+ tagMappers + ", jsdbDrivers=" + jdbcDrivers + "]";
	}

}
