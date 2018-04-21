package ru.snake.jsdb.db.mapper;

public class DbDummyMappers implements DbMappers {

	@Override
	public Object map(String fieldName, Object value) {
		return value;
	}

	@Override
	public String toString() {
		return "DbDummyMappers []";
	}

}
