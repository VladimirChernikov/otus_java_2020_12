package ru.otus.services.exceptions;

public class AppServicesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AppServicesException(Exception ex) {
        super(ex);
    }

    public AppServicesException(String msg) {
        super(msg);
    }
}
