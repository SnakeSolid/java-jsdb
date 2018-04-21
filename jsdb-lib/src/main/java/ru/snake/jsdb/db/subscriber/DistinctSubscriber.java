package ru.snake.jsdb.db.subscriber;

import java.util.HashSet;
import java.util.Set;

import ru.snake.jsdb.flow.Publisher;

public final class DistinctSubscriber<T> extends DbSubscriber<T, Set<T>> {

	private final Set<T> items;

	public DistinctSubscriber(Publisher<T> publisher) {
		super(publisher);

		this.items = new HashSet<>();
	}

	@Override
	public void onNext(T value) {
		this.items.add(value);
	}

	@Override
	protected Set<T> getResult() {
		return this.items;
	}

	@Override
	public String toString() {
		return "DistinctSubscriber [items=" + items + "]";
	}

}
