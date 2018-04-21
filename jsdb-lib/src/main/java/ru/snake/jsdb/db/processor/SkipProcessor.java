package ru.snake.jsdb.db.processor;

import ru.snake.jsdb.flow.Publisher;

public final class SkipProcessor<T> extends DbProcessor<T, T> {

	private final int count;

	private int n;

	public SkipProcessor(Publisher<T> publisher, int count) {
		super(publisher);

		this.count = count;
		this.n = 0;
	}

	@Override
	public void onNext(T value) {
		this.n += 1;

		if (this.n > this.count) {
			this.fireNext(value);
		}
	}

	@Override
	public String toString() {
		return "SkipProcessor [count=" + count + ", n=" + n + "]";
	}

}
