package ru.snake.jsdb.flow;

public abstract class Processor<I, O> extends Publisher<O> implements Subscriber<I> {

	protected final Publisher<I> publisher;

	public Processor(Publisher<I> publisher) {
		this.publisher = publisher;

		publisher.subscribe(this);
	}

	@Override
	public void onError(Throwable error) {
		fireError(error);
	}

	@Override
	public void onComplete() {
		fireComplete();
	}

	@Override
	public void consume() {
		this.publisher.consume();
	}

}
