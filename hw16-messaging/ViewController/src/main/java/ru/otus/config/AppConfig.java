package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.GetClientResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.services.ClientService;
import ru.otus.services.handlers.GetClientRequestHandler;
import ru.otus.services.handlers.SaveClientRequestHandler;

@Configuration
public class AppConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";


    // common

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl(true);
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }
    
    ////


    // front end

    @Bean
    public HandlersStore requestHandlerFrontendStore() {
        return new HandlersStoreImpl();
    }

    @Bean
    public MsClient frontendMsClient( MessageSystem messageSystem, CallbackRegistry callbackRegistry, HandlersStore requestHandlerFrontendStore ) {

        requestHandlerFrontendStore.addHandler( MessageType.GET_CLIENT,  new GetClientResponseHandler( callbackRegistry ) );
        requestHandlerFrontendStore.addHandler( MessageType.PUT_CLIENT,  new GetClientResponseHandler( callbackRegistry ) );

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontendStore, callbackRegistry);

        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl( frontendMsClient, DATABASE_SERVICE_CLIENT_NAME );
    }

    ////


    // back end

    @Bean
    public HandlersStore requestHandlerDatabaseStore() {
        return new HandlersStoreImpl();
    }

    @Bean
    public MsClient databaseMsClient( MessageSystem messageSystem, CallbackRegistry callbackRegistry, HandlersStore requestHandlerDatabaseStore, ClientService clientService ) {

        requestHandlerDatabaseStore.addHandler( MessageType.GET_CLIENT, new GetClientRequestHandler( clientService ) );
        requestHandlerDatabaseStore.addHandler( MessageType.PUT_CLIENT, new SaveClientRequestHandler( clientService ) );

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabaseStore, callbackRegistry);

        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
    
    ////


}
