package ru.snake.jsdb.lib.db.flow;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import ru.snake.jsdb.lib.db.visitor.CollectVisitor;
import ru.snake.jsdb.lib.db.visitor.CountVisitor;
import ru.snake.jsdb.lib.db.visitor.DistinctVisitor;
import ru.snake.jsdb.lib.db.visitor.EachVisitor;
import ru.snake.jsdb.lib.db.visitor.FoldVisitor;
import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.DbFlow;
import ru.snake.jsdb.lib.flow.DbSource;

/**
 * Basic data flow class to provide default flow methods such as
 * {@link DbFlow#filter(Predicate)}, {@link DbFlow#map(Function)},
 * {@link DbFlow#collect()} and so on.
 *
 * @author snake
 *
 * @param <T>
 *            flow element type
 */
public abstract class AbstractDbFlow<T> implements DbFlow<T>, DbSource<T> {

	@Override
	public final DbFlow<T> take(final int count) {
		return new TakeFlow<>(this, count);
	}

	@Override
	public final DbFlow<T> skip(final int count) {
		return new SkipFlow<>(this, count);
	}

	@Override
	public final <R> DbFlow<R> map(final Function<T, R> mapper) {
		return new MapFlow<>(this, mapper);
	}

	@Override
	public final DbFlow<T> filter(final Predicate<T> filter) {
		return new FilterFlow<>(this, filter);
	}

	@Override
	public final <A> A fold(final A accumulator, final BiFunction<A, T, A> reducer) throws ScriptExecutionException {
		return new FoldVisitor<>(this, accumulator, reducer).get();
	}

	@Override
	public final void forEach(final Consumer<T> consumer) throws ScriptExecutionException {
		new EachVisitor<>(this, consumer).get();
	}

	@Override
	public final List<T> collect() throws ScriptExecutionException {
		return new CollectVisitor<>(this).get();
	}

	@Override
	public final Set<T> distinct() throws ScriptExecutionException {
		return new DistinctVisitor<>(this).get();
	}

	@Override
	public final long count() throws ScriptExecutionException {
		return new CountVisitor<>(this).get();
	}

}
