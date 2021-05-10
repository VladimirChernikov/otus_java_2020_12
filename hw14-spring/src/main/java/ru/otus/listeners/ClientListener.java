package ru.otus.listeners;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.domain.events.ClientCompleteEvent;

@Service
@Slf4j
public class ClientListener {

    @Async
    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void handleClientCompletedEvent(ClientCompleteEvent event) {
        log.info("TODO: Processing event {}...", event);
    }

}
