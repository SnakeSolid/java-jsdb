package ru.snake.jsdb.lib;

import java.util.Collection;

/**
 * Preconditions checks to validate method parameters and results.
 *
 * @author snake
 *
 */
public final class Preconditions {

	/**
	 * Hide public constructor.
	 */
	private Preconditions() {
	}

	/**
	 * If check is false throws {@link IllegalArgumentException} with given
	 * message.
	 *
	 * @param check
	 *            check
	 * @param message
	 *            message
	 */
	public static void argument(final boolean check, final String message) {
		if (!check) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * If value is null throws {@link IllegalArgumentException} with message -
	 * "argument must not be null".
	 *
	 * @param value
	 *            value
	 * @param name
	 *            name
	 */
	public static void argumentNotNull(final Object value, final String name) {
		if (value == null) {
			throw new IllegalArgumentException(String.format("Argument %s must not be null", name));
		}
	}

	/**
	 * If value is empty string throws {@link IllegalArgumentException} with
	 * message - "argument must not be empty string".
	 *
	 * @param value
	 *            value
	 * @param name
	 *            name
	 */
	public static void argumentNotEmpty(final String value, final String name) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException(String.format("Argument %s must not be empty string", name));
		}
	}

	/**
	 * If value is empty collection throws {@link IllegalArgumentException} with
	 * message - "argument must not be empty string".
	 *
	 * @param value
	 *            value
	 * @param name
	 *            name
	 */
	public static void argumentNotEmpty(final Collection<?> value, final String name) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException(String.format("Argument %s must not be empty collection", name));
		}
	}

}
