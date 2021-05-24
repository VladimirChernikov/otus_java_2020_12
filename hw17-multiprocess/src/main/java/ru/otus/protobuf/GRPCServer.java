package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RemoteGeneratorServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteGeneratorServiceImpl remoteGeneratorService = new RemoteGeneratorServiceImpl();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteGeneratorService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
