package ru.otus.server;

import java.util.Arrays;

import com.google.gson.Gson;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ru.otus.services.ClientService;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

public class CrmWebServerWithFilterBasedSecurity extends CrmWebServerSimple {
    private final UserAuthService authService;

    public CrmWebServerWithFilterBasedSecurity(int port,
                                                 UserAuthService authService,
                                                 ClientService clientService,
                                                 Gson gson,
                                                 TemplateProcessor templateProcessor,
                                                 ErrorHandler errorHandler) {
        super(port, clientService, gson, templateProcessor, errorHandler);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
