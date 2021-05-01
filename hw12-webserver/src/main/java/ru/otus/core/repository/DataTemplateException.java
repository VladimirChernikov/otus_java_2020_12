package ru.otus.core.repository;

public class DataTemplateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataTemplateException(Exception ex) {
        super(ex);
    }
}
