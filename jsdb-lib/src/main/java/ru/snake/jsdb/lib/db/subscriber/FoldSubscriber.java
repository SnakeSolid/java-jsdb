package ru.snake.jsdb.lib.db.subscriber;

import java.util.function.BiFunction;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.Publisher;

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
	protected A getResult() throws ScriptExecutionException {
		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.accumulator;
	}

	@Override
	public String toString() {
		return "FoldSubscriber [reducer=" + reducer + ", accumulator=" + accumulator + "]";
	}

}
