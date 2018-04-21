package ru.snake.jsdb.db.processor;

import java.util.function.Function;

import ru.snake.jsdb.flow.Publisher;

public final class MapProcessor<I, O> extends DbProcessor<I, O> {

	protected final Function<I, O> mapper;

	public MapProcessor(Publisher<I> publisher, Function<I, O> mapper) {
		super(publisher);

		this.mapper = mapper;
	}

	@Override
	public void onNext(I value) {
		try {
			O result = this.mapper.apply(value);

			this.fireNext(result);
		} catch (Exception e) {
			this.publisher.cancel();

			fireError(e);
		}
	}

	@Override
	public String toString() {
		return "MapProcessor [mapper=" + mapper + "]";
	}

}
