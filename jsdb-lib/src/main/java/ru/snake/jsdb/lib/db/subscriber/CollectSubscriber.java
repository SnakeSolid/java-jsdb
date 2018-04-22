package ru.snake.jsdb.lib.db.subscriber;

import java.util.ArrayList;
import java.util.List;

import ru.snake.jsdb.lib.error.ScriptExecutionException;
import ru.snake.jsdb.lib.flow.Publisher;

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
	protected List<T> getResult() throws ScriptExecutionException {
		if (this.error.isPresent()) {
			throw new ScriptExecutionException(this.error.get());
		}

		return this.items;
	}

	@Override
	public String toString() {
		return "CollectSubscriber [items=" + items + "]";
	}

}
