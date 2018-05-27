package ru.snake.jsdb.lib.db;

import java.util.HashMap;
import java.util.Map;

public final class DbRow extends HashMap<String, Object> implements Map<String, Object> {

	private static final long serialVersionUID = 1811049707269389599L;

	public DbRow(int initialCapacity) {
		super(initialCapacity);
	}

}
