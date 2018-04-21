package ru.snake.jsdb.db.subscriber;

import java.util.function.Consumer;

import ru.snake.jsdb.flow.Publisher;

public final class EachSubscriber<T> extends DbSubscriber<T, Void> {

	private final Consumer<T> consumer;

	public EachSubscriber(Publisher<T> publisher, Consumer<T> consumer) {
		super(publisher);

		this.consumer = consumer;
	}

	@Override
	public void onNext(T value) {
		this.consumer.accept(value);
	}

	@Override
	protected Void getResult() {
		return null;
	}

	@Override
	public String toString() {
		return "EachSubscriber [consumer=" + consumer + "]";
	}

}
