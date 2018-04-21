package ru.snake.jsdb.db.processor;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import ru.snake.jsdb.db.subscriber.CollectSubscriber;
import ru.snake.jsdb.db.subscriber.CountSubscriber;
import ru.snake.jsdb.db.subscriber.DistinctSubscriber;
import ru.snake.jsdb.db.subscriber.EachSubscriber;
import ru.snake.jsdb.db.subscriber.FoldSubscriber;
import ru.snake.jsdb.flow.Publisher;

public abstract class DbPublisher<T> extends Publisher<T> {

	public DbProcessor<T, T> take(int count) {
		return new TakeProcessor<>(this, count);
	}

	public DbProcessor<T, T> skip(int count) {
		return new SkipProcessor<>(this, count);
	}

	public <R> DbProcessor<T, R> map(Function<T, R> mapper) {
		return new MapProcessor<>(this, mapper);
	}

	public DbProcessor<T, T> filter(Predicate<T> filter) {
		return new FilterProcessor<>(this, filter);
	}

	public <A> A fold(A accumulator, BiFunction<A, T, A> reducer) {
		return new FoldSubscriber<>(this, accumulator, reducer).get();
	}

	public Void forEach(Consumer<T> consumer) {
		return new EachSubscriber<>(this, consumer).get();
	}

	public List<T> collect() {
		return new CollectSubscriber<>(this).get();
	}

	public Set<T> distinct() {
		return new DistinctSubscriber<>(this).get();
	}

	public Integer count() {
		return new CountSubscriber<>(this).get();
	}

}
