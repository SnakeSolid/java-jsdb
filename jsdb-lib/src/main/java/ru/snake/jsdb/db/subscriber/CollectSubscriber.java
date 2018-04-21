package ru.snake.jsdb.db.subscriber;

import java.util.ArrayList;
import java.util.List;

import ru.snake.jsdb.flow.Publisher;

public final class CollectSubscriber<T> extends DbSubscriber<T, List<T>> {

	private final List<T> items;

	public CollectSubscriber(Publisher<T> publisher) {
		super(publisher);

		this.items = new ArrayList<>();
	}

	@Override
	public void onNext(T value) {
		this.items.add(value);
	}

	@Override
	protected List<T> getResult() {
		return this.items;
	}

	@Override
	public String toString() {
		return "CollectSubscriber [items=" + items + "]";
	}

}
