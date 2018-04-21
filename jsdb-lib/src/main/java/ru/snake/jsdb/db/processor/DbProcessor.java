package ru.snake.jsdb.db.processor;

import java.util.Optional;

import ru.snake.jsdb.flow.Publisher;
import ru.snake.jsdb.flow.Subscriber;

public abstract class DbProcessor<I, O> extends DbPublisher<O> implements Subscriber<I> {

	protected final Publisher<I> publisher;

	protected Optional<Throwable> error;

	public DbProcessor(Publisher<I> publisher) {
		this.publisher = publisher;
		this.error = Optional.empty();

		publisher.subscribe(this);
	}

	@Override
	public void onError(Throwable error) {
		this.error = Optional.of(error);
		this.publisher.cancel();
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void consume() {
		this.publisher.consume();
	}

}
