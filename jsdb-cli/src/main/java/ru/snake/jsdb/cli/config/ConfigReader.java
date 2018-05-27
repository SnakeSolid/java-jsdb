package ru.snake.jsdb.cli.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import io.ous.jtoml.JToml;
import io.ous.jtoml.Toml;
import io.ous.jtoml.TomlTable;

public class ConfigReader {

	/**
	 * Read configuration from giver reader.
	 *
	 * @param reader
	 *            reader
	 * @return configuration
	 * @throws IOException
	 *             if error occurred
	 */
	public static Configuration read(Reader reader) throws IOException {
		Toml toml = JToml.parse(reader);
		Configuration configuration = new Configuration();

		Optional.ofNullable(toml.getList("library_path")).orElseGet(Collections::emptyList).forEach(
				path -> configuration.addLibraryPath(path.toString()));

		configuration.setJdbcDriver(toml.getString("jdbc", "driver"));
		configuration.setJdbcUrl(toml.getString("jdbc", "url"));

		Optional.ofNullable(toml.getTomlTable("field_mappers")).orElseGet(TomlTable::new).forEach(
				(tableName, fieldMappers) -> readTableField(configuration, tableName, fieldMappers));

		Optional.ofNullable(toml.getTomlTable("tag_mappers")).orElseGet(TomlTable::new).forEach(
				(tableTag, mapperClass) -> configuration.addTagMapper(tableTag, String.valueOf(mapperClass)));

		return configuration;
	}

	private static void readTableField(Configuration configuration, String tableName, Object fieldMappers) {
		if (fieldMappers instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> mappers = (Map<Object, Object>) fieldMappers;

			mappers.forEach((fieldName, mapperClass) -> configuration.addFieldMapper(tableName,
					String.valueOf(fieldName), String.valueOf(mapperClass)));
		}
	}

}
