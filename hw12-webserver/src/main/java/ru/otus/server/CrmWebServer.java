package ru.otus.server;

public interface CrmWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
