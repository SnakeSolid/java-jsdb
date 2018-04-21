package ru.snake.jsdb.flow;

public interface Subscriber<T> {

	public void onNext(T value);

	public void onError(Throwable error);

	public void onComplete();

}
