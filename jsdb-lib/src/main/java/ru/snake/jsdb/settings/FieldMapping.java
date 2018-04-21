package ru.snake.jsdb.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.snake.jsdb.Preconditions;

public class FieldMapping {

	private final Map<String, String> mapping;

	public FieldMapping() {
		this.mapping = new HashMap<>();
	}

	public boolean isEmpty() {
		return this.mapping.isEmpty();
	}

	public Set<String> fieldNames() {
		return Collections.unmodifiableSet(this.mapping.keySet());
	}

	public boolean contains(String fieldName) {
		Preconditions.argumentNotNull(fieldName, "fieldName");

		return this.mapping.containsKey(fieldName);
	}

	public String get(String fieldName) {
		Preconditions.argumentNotNull(fieldName, "fieldName");

		return this.mapping.get(fieldName);
	}

	public void insert(String fieldName, String mapperClass) {
		Preconditions.argumentNotNull(fieldName, "fieldName");
		Preconditions.argumentNotEmpty(fieldName, "fieldName");
		Preconditions.argumentNotNull(mapperClass, "mapperClass");

		this.mapping.put(fieldName, mapperClass);
	}

	public void remove(String fieldName) {
		Preconditions.argumentNotNull(fieldName, "fieldName");

		this.mapping.remove(fieldName);
	}

	@Override
	public String toString() {
		return "FieldMapping [mapping=" + mapping + "]";
	}

}
