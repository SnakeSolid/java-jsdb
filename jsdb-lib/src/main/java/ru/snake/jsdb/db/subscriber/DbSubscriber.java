package ru.snake.jsdb.db.subscriber;

import java.util.Optional;
import java.util.function.Supplier;

import ru.snake.jsdb.flow.Publisher;
import ru.snake.jsdb.flow.Subscriber;

public abstract class DbSubscriber<T, R> implements Subscriber<T>, Supplier<R> {

	protected final Publisher<T> publisher;

	protected Optional<Throwable> error;

	public DbSubscriber(Publisher<T> publisher) {
		this.publisher = publisher;

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

	public final R get() {
		this.publisher.consume();

		return getResult();
	}

	protected abstract R getResult();

}
