package ru.snake.jsdb.lib.flow;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Publisher<T> {

	private final Collection<Subscriber<? super T>> subscribers;

	protected boolean cancelled;

	public Publisher() {
		this.subscribers = new ArrayList<>(1);
		this.cancelled = false;
	}

	public void subscribe(Subscriber<? super T> subscriber) {
		subscribers.add(subscriber);
	}

	public void fireNext(T value) {
		for (Subscriber<? super T> subscriber : this.subscribers) {
			subscriber.onNext(value);
		}
	}

	public void fireError(Throwable error) {
		for (Subscriber<? super T> subscriber : this.subscribers) {
			subscriber.onError(error);
		}
	}

	public void fireComplete() {
		for (Subscriber<? super T> subscriber : this.subscribers) {
			subscriber.onComplete();
		}
	}

	public void cancel() {
		this.cancelled = true;
	}

	public abstract void consume();

}
