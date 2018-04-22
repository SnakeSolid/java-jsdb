package ru.snake.jsdb.lib.db.subscriber;

import java.util.Optional;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.Publisher;
import ru.snake.jsdb.lib.flow.Subscriber;

public abstract class DbSubscriber<T, R> implements Subscriber<T> {

	protected final Publisher<T> publisher;

	protected Optional<Throwable> error;

	public DbSubscriber(Publisher<T> publisher) {
		this.publisher = publisher;
		this.error = Optional.empty();

		publisher.subscribe(this);
	}

	@Override
	public final void onError(Throwable error) {
		this.error = Optional.of(error);
		this.publisher.cancel();
	}

	@Override
	public void onComplete() {
	}

	public final R get() throws ScriptExecutionException {
		this.publisher.consume();

		return getResult();
	}

	protected abstract R getResult() throws ScriptExecutionException;

}
