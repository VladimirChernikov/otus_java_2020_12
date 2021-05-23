package ru.otus.messagesystem.message;

public enum MessageType {
     VOID_MESSAGE("voidTechnicalMessage")
    ,GET_CLIENT("GetClient")
    ,PUT_CLIENT("SaveClient")
    ;

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
