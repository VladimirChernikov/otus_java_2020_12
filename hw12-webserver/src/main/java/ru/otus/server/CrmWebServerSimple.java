package ru.otus.server;

import com.google.gson.Gson;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.ClientService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientsApiServlet;
import ru.otus.servlet.ClientsServlet;


public class CrmWebServerSimple implements CrmWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final ClientService clientService;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public CrmWebServerSimple(int port, ClientService clientService, Gson gson, TemplateProcessor templateProcessor, ErrorHandler errorHandler) {
        this.clientService = clientService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        server.setErrorHandler( errorHandler );
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/crm", "/crm/api/v1/client/*"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, clientService)), "/crm");
        servletContextHandler.addServlet(new ServletHolder(new ClientsApiServlet(clientService, gson)), "/crm/api/v1/client/*");
        return servletContextHandler;
    }
}
