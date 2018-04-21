package ru.snake.jsdb.lib;

import java.util.Collection;
import java.util.Map;

public final class Preconditions {

	private Preconditions() {
	}

	public static void argument(boolean check, String message) {
		if (!check) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void argument(boolean check, String format, Object... args) {
		if (!check) {
			throw new IllegalArgumentException(String.format(format, args));
		}
	}

	public static void argumentNotNull(Object value, String name) {
		if (value == null) {
			throw new IllegalArgumentException(String.format("Argument %s must not be null", name));
		}
	}

	public static void argumentNotEmpty(String value, String name) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException(String.format("Argument %s must not be empty string", name));
		}
	}

	public static void argumentNotEmpty(Collection<?> value, String name) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException(String.format("Argument %s must not be empty collection", name));
		}
	}

	public static void argumentNotEmpty(Map<?, ?> value, String name) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException(String.format("Argument %s must not be empty map", name));
		}
	}

}
