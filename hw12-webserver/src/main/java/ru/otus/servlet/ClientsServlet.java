package ru.otus.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.services.ClientService;
import ru.otus.services.TemplateProcessor;


public class ClientsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String USERS_PAGE_TEMPLATE = "crm.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final ClientService clientService;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, ClientService clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put( TEMPLATE_ATTR_CLIENTS, this.clientService.findAll() );

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
