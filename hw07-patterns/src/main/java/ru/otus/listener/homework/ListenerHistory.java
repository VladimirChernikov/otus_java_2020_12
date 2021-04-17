package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.MessageStorage;

public class ListenerHistory implements Listener {

    private final MessageStorage messageStorage;

    public ListenerHistory( MessageStorage messageStorage ) {
        this.messageStorage = messageStorage;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        this.messageStorage.store( oldMsg.copy(), newMsg.copy() );
    }

}
