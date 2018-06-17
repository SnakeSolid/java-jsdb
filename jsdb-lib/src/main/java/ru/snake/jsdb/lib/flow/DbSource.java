package ru.snake.jsdb.lib.flow;

/**
 * Initial source of all flow elements.
 *
 * @author snake
 *
 * @param <T>
 *            element type
 */
public interface DbSource<T> {

	/**
	 * For every element from internal data source call corresponding visitor
	 * methods. If element valid calls {@link DbVisitor#acceptNext(Object)}. If
	 * error occurred calls
	 * {@link DbVisitor#acceptError(java.sql.SQLException)}. When internal
	 * source completed calls {@link DbVisitor#complete()}.
	 *
	 * @param visitor
	 *            visitor
	 */
	void accept(DbVisitor<T> visitor);

}
