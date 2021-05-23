package ru.otus.front;

import ru.otus.domain.Client;
import ru.otus.dto.ClientMessageData;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {

	public String putClient(Client client, MessageCallback<ClientMessageData> dataConsumer) ;
	public String getClientById(long id, MessageCallback<ClientMessageData> dataConsumer) ;
}

