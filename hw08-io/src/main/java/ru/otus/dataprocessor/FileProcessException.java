package ru.otus.dataprocessor;

public class FileProcessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileProcessException(Exception ex) {
        super(ex);
    }

    public FileProcessException(String msg) {
        super(msg);
    }
}
