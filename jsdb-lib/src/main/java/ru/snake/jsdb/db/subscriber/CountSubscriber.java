package ru.snake.jsdb.db.subscriber;

import ru.snake.jsdb.flow.Publisher;

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
	protected Integer getResult() {
		return this.count;
	}

	@Override
	public String toString() {
		return "CountSubscriber [count=" + count + "]";
	}

}
