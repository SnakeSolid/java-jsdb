package ru.snake.jsdb.lib.db.processor;

import ru.snake.jsdb.lib.flow.Publisher;

public final class TakeProcessor<T> extends DbProcessor<T, T> {

	private final int count;

	private int n;

	public TakeProcessor(Publisher<T> publisher, int count) {
		super(publisher);

		this.count = count;
		this.n = 0;
	}

	@Override
	public void onNext(T value) {
		this.n += 1;

		if (this.n > this.count) {
			this.publisher.cancel();
		} else {
			this.fireNext(value);
		}
	}

	@Override
	public String toString() {
		return "TakeProcessor [count=" + count + ", n=" + n + "]";
	}

}
