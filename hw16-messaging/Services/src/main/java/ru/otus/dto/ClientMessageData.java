package ru.otus.dto;

import ru.otus.domain.Client;
import ru.otus.messagesystem.client.ResultDataType;

public class ClientMessageData extends ResultDataType {
	private static final long serialVersionUID = 1L;

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_PENDING = "PENDING";

	private final Client client;
    private final String status;
    private final String errorMessage;

    public ClientMessageData(Client client, String status, String errorMessage) {
        this.client = client;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public ClientMessageData(Client client, String status) {
        this.client = client;
        this.status = status;
        this.errorMessage = "";
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Client getClient() {
        return client;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
}
