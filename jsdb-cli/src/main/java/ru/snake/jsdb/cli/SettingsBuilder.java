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

public class SettingsBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SettingsBuilder.class);

	private final Set<String> libraryPaths;

	private final Map<String, Map<String, String>> fieldMappers;

	private final Map<String, String> tagMappers;

	private Set<String> jdbcDrivers;

	public SettingsBuilder() {
		this.libraryPaths = new HashSet<>();
		this.fieldMappers = new HashMap<>();
		this.tagMappers = new HashMap<>();
		this.jdbcDrivers = new HashSet<>();
	}

	public SettingsBuilder withArguments(Arguments arguments) {
		Map<String, String> argFieldMappers = arguments.getFieldMappers();
		String jdbcDriver = arguments.getJdbcDriver();

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

		if (jdbcDriver != null) {
			this.jdbcDrivers.add(jdbcDriver);
		}

		return this;
	}

	public SettingsBuilder withConfiguration(Configuration config) {
		Map<String, Map<String, String>> confFieldMappers = config.getFieldMappers();
		String jdbcDriver = config.getJdbcDriver();

		this.libraryPaths.addAll(config.getLibraryPaths());

		confFieldMappers.forEach((tableName,
				fields) -> this.fieldMappers.computeIfAbsent(tableName, e -> new TreeMap<>()).putAll(fields));

		this.tagMappers.putAll(config.getTagMappers());

		if (jdbcDriver != null) {
			this.jdbcDrivers.add(jdbcDriver);
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
