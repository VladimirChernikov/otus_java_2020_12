package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
