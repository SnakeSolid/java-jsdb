package ru.snake.jsdb.lib.flow;

public abstract class Publisher<T> {

	private Subscriber<? super T> subscriber;

	protected boolean cancelled;

	public Publisher() {
		this.cancelled = false;
	}

	public void subscribe(Subscriber<? super T> subscriber) {
		this.subscriber = subscriber;
	}

	public void fireNext(T value) {
		if (this.subscriber != null) {
			subscriber.onNext(value);
		}
	}

	public void fireError(Throwable error) {
		if (this.subscriber != null) {
			subscriber.onError(error);
		}
	}

	public void fireComplete() {
		if (this.subscriber != null) {
			subscriber.onComplete();
		}
	}

	public void cancel() {
		this.cancelled = true;
	}

	public abstract void consume();

}
