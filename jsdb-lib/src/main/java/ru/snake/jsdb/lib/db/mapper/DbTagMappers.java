package ru.snake.jsdb.lib.db.mapper;

import java.util.HashMap;
import java.util.Map;

import ru.snake.jsdb.lib.Preconditions;

public class DbTagMappers implements DbMappers {

	private Map<String, DbMapper> mappers;

	public DbTagMappers() {
		this.mappers = new HashMap<>();
	}

	public void insert(String tagName, DbMapper mapper) {
		Preconditions.argumentNotNull(tagName, "tagName");
		Preconditions.argumentNotEmpty(tagName, "tagName");
		Preconditions.argumentNotNull(mapper, "mapper");

		this.mappers.put(tagName, mapper);
	}

	@Override
	public Object map(String tagName, Object value) {
		Preconditions.argumentNotNull(tagName, "tagName");

		DbMapper fieldMapper = this.mappers.get(tagName);

		if (fieldMapper != null) {
			return fieldMapper.map(value);
		}

		return value;
	}

	@Override
	public String toString() {
		return "DbTagMappers [mappers=" + mappers + "]";
	}

}
