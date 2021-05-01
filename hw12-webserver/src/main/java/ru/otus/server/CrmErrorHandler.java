package ru.otus.server;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.jetty.server.handler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class CrmErrorHandler extends ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmErrorHandler.class);

    @Override
    protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        log.error("Request = {}, session = {}, code = {}, message = {}", request, request.getSession(), code, message);
        // TODO: create an incident
    }

}

