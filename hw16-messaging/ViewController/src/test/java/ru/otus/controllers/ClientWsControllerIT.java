package ru.otus.controllers;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import ru.otus.domain.Client;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientWsControllerIT {

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String SEND_PUT_CLIENT_ENDPOINT = "/app/client";
    private static final String SEND_GET_CLIENT_BY_ID_ENDPOINT = "/app/client.";
    private static final String SUBSCRIBE_STATUS_ENDPOINT = "/topic/status/";
    private static final String SUBSCRIBE_CLIENTS_SESSION_ENDPOINT = "/topic/clients/";
    private static final String SUBSCRIBE_CLIENTS_ALL_ENDPOINT = "/topic/clients";

    private CompletableFuture<Client> completableFuture;

    @BeforeEach
    public void setup() {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/websocket";
    }


    @Test
    public void testPutClientEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        //given
        String sessionId = "12345678123456678";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_CLIENTS_ALL_ENDPOINT, new CreateClientStompFrameHandler());

        StompHeaders headers  = new StompHeaders();
        headers.set("sessionId", sessionId);
        headers.setDestination(SEND_PUT_CLIENT_ENDPOINT);

        //when
        stompSession.send(headers, Client.builder().name("A1234").build());

        //then
        Client client = completableFuture.get(1, SECONDS);
        assertNotNull(client);
    }

    @Test
    public void testPutClientStatusEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        //given
        String sessionId = "12345678123456678";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_STATUS_ENDPOINT + sessionId, new CreateClientStompFrameHandler());

        StompHeaders headers  = new StompHeaders();
        headers.set("sessionId", sessionId);
        headers.setDestination(SEND_PUT_CLIENT_ENDPOINT);

        //when
        stompSession.send(headers, Client.builder().name("A1234").build());

        //then
        Client client = completableFuture.get(1, SECONDS);
        System.out.println(client);
        assertNotNull(client);
    }

    @Test
    public void testGetClientByIdEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

        //given
        
        // put client
        String sessionId = "12345678123456678";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_CLIENTS_ALL_ENDPOINT, new CreateClientStompFrameHandler());

        StompHeaders headers  = new StompHeaders();
        headers.set("sessionId", sessionId);
        headers.setDestination(SEND_PUT_CLIENT_ENDPOINT);

        stompSession.send(headers, Client.builder().name("A1234").build());

        Client clientSaved = completableFuture.get(1, SECONDS);

        long clientId = clientSaved.getId();
        //

        // get client
        completableFuture = new CompletableFuture<>();
        stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
        stompSession.subscribe(SUBSCRIBE_CLIENTS_SESSION_ENDPOINT + sessionId, new CreateClientStompFrameHandler());

        headers  = new StompHeaders();
        headers.set("sessionId", sessionId);
        headers.setDestination(SEND_GET_CLIENT_BY_ID_ENDPOINT + clientId);

        //when
        stompSession.send(headers, null);

        //then
        Client client = completableFuture.get(1, SECONDS);
        assertNotNull(client);

    }


    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateClientStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Client.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Client) o);
        }
    }

}
