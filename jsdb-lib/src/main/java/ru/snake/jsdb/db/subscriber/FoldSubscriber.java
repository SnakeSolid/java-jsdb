package ru.snake.jsdb.db.subscriber;

import java.util.function.BiFunction;

import ru.snake.jsdb.flow.Publisher;

public final class FoldSubscriber<A, T> extends DbSubscriber<T, A> {

	private final BiFunction<A, T, A> reducer;

	private A accumulator;

	public FoldSubscriber(Publisher<T> publisher, A accumulator, BiFunction<A, T, A> reducer) {
		super(publisher);

		this.reducer = reducer;
		this.accumulator = accumulator;
	}

	@Override
	public void onNext(T value) {
		this.accumulator = this.reducer.apply(this.accumulator, value);
	}

	@Override
	protected A getResult() {
		return this.accumulator;
	}

	@Override
	public String toString() {
		return "FoldSubscriber [reducer=" + reducer + ", accumulator=" + accumulator + "]";
	}

}
