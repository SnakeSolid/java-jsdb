package ru.snake.jsdb.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.snake.jsdb.Preconditions;

public class TableMapping {

	private Map<String, FieldMapping> mapping;

	public TableMapping() {
		this.mapping = new HashMap<>();
	}

	public boolean isEmpty() {
		return this.mapping.isEmpty();
	}

	public Set<String> tableNames() {
		return Collections.unmodifiableSet(this.mapping.keySet());
	}

	public boolean contains(String tableName) {
		Preconditions.argumentNotNull(tableName, "tableName");

		return this.mapping.containsKey(tableName);
	}

	public FieldMapping get(String tableName) {
		Preconditions.argumentNotNull(tableName, "tableName");

		return this.mapping.get(tableName);
	}

	public void insert(String tableName, FieldMapping fieldMapping) {
		Preconditions.argumentNotNull(tableName, "tableName");
		Preconditions.argumentNotEmpty(tableName, "tableName");
		Preconditions.argumentNotNull(fieldMapping, "fieldMapping");
		Preconditions.argument(!fieldMapping.isEmpty(), "Argument fieldMapping must not be empty");

		this.mapping.put(tableName, fieldMapping);
	}

	public void remove(String tableName) {
		Preconditions.argumentNotNull(tableName, "tableName");

		this.mapping.remove(tableName);
	}

	@Override
	public String toString() {
		return "TableMapping [mapping=" + mapping + "]";
	}

}
