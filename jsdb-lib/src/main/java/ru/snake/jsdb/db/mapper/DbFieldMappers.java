package ru.snake.jsdb.db.mapper;

import java.util.HashMap;
import java.util.Map;

import ru.snake.jsdb.Preconditions;

public class DbFieldMappers implements DbMappers {

	private Map<String, DbMapper> mappers;

	public DbFieldMappers() {
		this.mappers = new HashMap<>();
	}

	public void insert(String fieldName, DbMapper mapper) {
		Preconditions.argumentNotNull(fieldName, "fieldName");
		Preconditions.argumentNotEmpty(fieldName, "fieldName");
		Preconditions.argumentNotNull(mapper, "mapper");

		this.mappers.put(fieldName, mapper);
	}

	@Override
	public Object map(String fieldName, Object value) {
		Preconditions.argumentNotNull(fieldName, "fieldName");

		DbMapper fieldMapper = this.mappers.get(fieldName);

		if (fieldMapper != null) {
			return fieldMapper.map(value);
		}

		return value;
	}

	@Override
	public String toString() {
		return "DbFieldMappers [mappers=" + mappers + "]";
	}

}
