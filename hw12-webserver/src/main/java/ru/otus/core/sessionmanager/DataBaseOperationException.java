package ru.otus.core.sessionmanager;

public class DataBaseOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
