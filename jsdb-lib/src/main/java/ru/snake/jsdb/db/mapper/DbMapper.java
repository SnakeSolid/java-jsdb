package ru.snake.jsdb.db.mapper;

@FunctionalInterface
public interface DbMapper {

	public Object map(Object value);

}
