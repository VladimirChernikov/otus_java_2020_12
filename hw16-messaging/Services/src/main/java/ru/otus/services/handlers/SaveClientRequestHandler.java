package ru.otus.services.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.domain.exceptions.AppDomainException;
import ru.otus.dto.ClientMessageData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.services.ClientService;

public class SaveClientRequestHandler implements RequestHandler<ClientMessageData> {

    private static final Logger logger = LoggerFactory.getLogger(SaveClientRequestHandler.class);

    private final ClientService clientService;

    public SaveClientRequestHandler( ClientService clientService ) {
        this.clientService = clientService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientMessageData userData = MessageHelper.getPayload(msg);
        Message replay;
        ClientMessageData data;
        try {
            data = new ClientMessageData( clientService.save(userData.getClient()), ClientMessageData.STATUS_OK, "" );
        } catch ( AppDomainException e ){
            data = new ClientMessageData( userData.getClient(), ClientMessageData.STATUS_ERROR, e.toString() );
        } catch ( Exception e ) {
            data = new ClientMessageData( userData.getClient(), ClientMessageData.STATUS_ERROR, "An error occured. Please contact your system administrator." );
        }
        replay = MessageBuilder.buildReplyMessage(msg, data);
        return Optional.of(replay);
    }

}
