package ru.snake.jsdb.db.processor;

import java.util.function.Predicate;

import ru.snake.jsdb.flow.Publisher;

public final class FilterProcessor<T> extends DbProcessor<T, T> {

	protected final Predicate<T> filter;

	public FilterProcessor(Publisher<T> publisher, Predicate<T> filter) {
		super(publisher);

		this.filter = filter;
	}

	@Override
	public void onNext(T value) {
		try {
			if (this.filter.test(value)) {
				this.fireNext(value);
			}
		} catch (Exception e) {
			this.publisher.cancel();

			fireError(e);
		}
	}

	@Override
	public String toString() {
		return "FilterProcessor [filter=" + filter + "]";
	}

}
