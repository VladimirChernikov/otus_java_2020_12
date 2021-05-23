package ru.otus.front;

import ru.otus.domain.Client;
import ru.otus.dto.ClientMessageData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

public class FrontendServiceImpl implements FrontendService {

    private final MsClient frontendMsClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient frontendMsClient, String databaseServiceClientName) {
        this.frontendMsClient = frontendMsClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public String putClient(Client client, MessageCallback<ClientMessageData> dataConsumer) {
        Message outMsg = frontendMsClient.produceMessage(databaseServiceClientName, new ClientMessageData( client, ClientMessageData.STATUS_PENDING ),
                MessageType.PUT_CLIENT, dataConsumer);
        frontendMsClient.sendMessage(outMsg);
        return outMsg.getId().getId();
    }

    @Override
    public String getClientById(long id, MessageCallback<ClientMessageData> dataConsumer) {
        Client client = new Client(id);
        Message outMsg = frontendMsClient.produceMessage(databaseServiceClientName, new ClientMessageData( client, ClientMessageData.STATUS_PENDING ),
                MessageType.GET_CLIENT, dataConsumer);
        frontendMsClient.sendMessage(outMsg);
        return outMsg.getId().getId();
    }

}
