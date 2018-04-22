package ru.snake.jsdb.lib.db.subscriber;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.Publisher;

public final class CountSubscriber<T> extends DbSubscriber<T, Integer> {

	private int count;

	public CountSubscriber(Publisher<T> publisher) {
		super(publisher);

		this.count = 0;
	}

	@Override
	public void onNext(T value) {
		this.count += 1;
	}

	@Override
	protected Integer getResult() throws ScriptExecutionException {
		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.count;
	}

	@Override
	public String toString() {
		return "CountSubscriber [count=" + count + "]";
	}

}
