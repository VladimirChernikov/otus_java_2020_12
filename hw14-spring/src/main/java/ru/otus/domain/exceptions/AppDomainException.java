package ru.otus.domain.exceptions;

public class AppDomainException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AppDomainException(Exception ex) {
        super(ex);
    }

    public AppDomainException(String msg) {
        super(msg);
    }
}
