package ru.snake.jsdb.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.snake.jsdb.Preconditions;

public class TagMapping {

	private final Map<String, String> mapping;

	public TagMapping() {
		this.mapping = new HashMap<>();
	}

	public boolean isEmpty() {
		return this.mapping.isEmpty();
	}

	public Set<String> tagNames() {
		return Collections.unmodifiableSet(this.mapping.keySet());
	}

	public boolean contains(String tagName) {
		Preconditions.argumentNotNull(tagName, "tagName");

		return this.mapping.containsKey(tagName);
	}

	public String get(String tagName) {
		Preconditions.argumentNotNull(tagName, "tagName");

		return this.mapping.get(tagName);
	}

	public void insert(String tagName, String mapperClass) {
		Preconditions.argumentNotNull(tagName, "tagName");
		Preconditions.argumentNotEmpty(tagName, "tagName");
		Preconditions.argumentNotNull(mapperClass, "mapperClass");

		this.mapping.put(tagName, mapperClass);
	}

	public void remove(String tagName) {
		Preconditions.argumentNotNull(tagName, "tagName");

		this.mapping.remove(tagName);
	}

	@Override
	public String toString() {
		return "TagMapping [mapping=" + mapping + "]";
	}

}
