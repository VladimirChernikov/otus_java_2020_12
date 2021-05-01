package ru.otus.model;

public interface MessageStorage {
    public void store( Message oldMsg, Message newMsg );
}
