package ru.snake.jsdb.lib.db.subscriber;

import java.util.function.Consumer;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.Publisher;

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
	protected Void getResult() throws ScriptExecutionException {
		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return null;
	}

	@Override
	public String toString() {
		return "EachSubscriber [consumer=" + consumer + "]";
	}

}
