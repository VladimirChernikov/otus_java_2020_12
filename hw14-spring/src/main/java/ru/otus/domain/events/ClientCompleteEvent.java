package ru.otus.domain.events;

import lombok.Value;

@Value
public class ClientCompleteEvent {
    Long clientId;
}

