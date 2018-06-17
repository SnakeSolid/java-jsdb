package ru.snake.jsdb.lib.flow;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import ru.snake.jsdb.lib.error.ScriptExecutionException;

/**
 * Database data flow interface. Contains common methods for all available in
 * script query results.
 *
 * @author snake
 *
 * @param <T>
 *            produced type
 */
public interface DbFlow<T> {

	/**
	 * Returns another data flow which takes only required number of elements
	 * from source.
	 *
	 * @param count
	 *            count
	 * @return new data flow
	 */
	DbFlow<T> take(int count);

	/**
	 * Returns another data flow which skip first count of elements from source.
	 *
	 * @param count
	 *            count
	 * @return new data flow
	 */
	DbFlow<T> skip(int count);

	/**
	 * Returns another data flow which convert all elements from source. Mapper
	 * function will be applied to every element from source. Result of mapper
	 * execution will be returned as element of new flow.
	 *
	 * @param mapper
	 *            mapper function
	 * @return new data flow
	 * @param <R>
	 *            result flow element type
	 */
	<R> DbFlow<R> map(Function<T, R> mapper);

	/**
	 * Returns another data flow which contains only such elements from source
	 * where filter function returns <code>true</code>. Fulter function will be
	 * applied to every element from source.
	 *
	 * @param filter
	 *            filter function
	 * @return new data flow
	 */
	DbFlow<T> filter(Predicate<T> filter);

	/**
	 * Fully consume source flow. For every element from source call reducer
	 * with accumulator and current element value. Call reducer for every
	 * element from source flow. If source flow throws error - rethrows it
	 * wrapping to {@link ScriptExecutionException}.
	 *
	 * @param accumulator
	 *            initial accumulator value
	 * @param reducer
	 *            reducer function
	 * @return reduced flow as single object
	 * @throws ScriptExecutionException
	 *             if error occurred
	 * @param <A>
	 *            accumulator type
	 */
	<A> A fold(A accumulator, BiFunction<A, T, A> reducer) throws ScriptExecutionException;

	/**
	 * Call consumer for every element from source data flow. Fully consume
	 * source flow. If source flow throws error - rethrows it wrapping to
	 * {@link ScriptExecutionException}.
	 *
	 * @param consumer
	 *            consumer
	 * @throws ScriptExecutionException
	 *             if error occurred
	 */
	void forEach(Consumer<T> consumer) throws ScriptExecutionException;

	/**
	 * Returns list with all items from source data flow. Fully consume source
	 * flow. If source flow throws error - rethrows it wrapping to
	 * {@link ScriptExecutionException}.
	 *
	 * @return list of flow items
	 * @throws ScriptExecutionException
	 *             if error occurred
	 */
	List<T> collect() throws ScriptExecutionException;

	/**
	 * Returns set with all items from source data flow. Fully consume source
	 * flow. If source flow throws error - rethrows it wrapping to
	 * {@link ScriptExecutionException}.
	 *
	 * @return set of flow items
	 * @throws ScriptExecutionException
	 *             if error occurred
	 */
	Set<T> distinct() throws ScriptExecutionException;

	/**
	 * Returns number of elements in data flow. Fully consume source flow. If
	 * source flow throws error - rethrows it wrapping to
	 * {@link ScriptExecutionException}.
	 *
	 * @return number of elements
	 * @throws ScriptExecutionException
	 *             if error occurred
	 */
	long count() throws ScriptExecutionException;

}
