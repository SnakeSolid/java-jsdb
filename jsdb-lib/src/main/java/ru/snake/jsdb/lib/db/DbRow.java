package ru.snake.jsdb.lib.db;

import java.util.HashMap;

public final class DbRow extends HashMap<String, Object> {

	private static final long serialVersionUID = 1811049707269389599L;

	public DbRow(int initialCapacity) {
		super(initialCapacity);
	}

}
