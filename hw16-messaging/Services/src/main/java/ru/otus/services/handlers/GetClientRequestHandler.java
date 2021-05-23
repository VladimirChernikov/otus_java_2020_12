package ru.otus.services.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.domain.Client;
import ru.otus.dto.ClientMessageData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.services.ClientService;

public class GetClientRequestHandler implements RequestHandler<ClientMessageData> {

    private static final Logger logger = LoggerFactory.getLogger(GetClientRequestHandler.class);

    private final ClientService clientService;

    public GetClientRequestHandler( ClientService clientService ) {
        this.clientService = clientService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientMessageData userData = MessageHelper.getPayload(msg);
        long id = userData.getClient().getId();
        Client data = clientService.findById( id );
        if ( data == null )  {
            return Optional.of(MessageBuilder.buildReplyMessage(msg, new ClientMessageData( userData.getClient(), ClientMessageData.STATUS_ERROR, String.format("Клиент с ID = %d не найден!", id ) )));
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new ClientMessageData( data, ClientMessageData.STATUS_OK )));
    }

}
