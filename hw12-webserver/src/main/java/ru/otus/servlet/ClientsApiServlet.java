package ru.otus.servlet;

import java.io.IOException;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Client;
import ru.otus.services.ClientService;

public class ClientsApiServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ClientsApiServlet.class);

    private static final long serialVersionUID = 1L;

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final ClientService clientService;
    private final Gson gson;

    public ClientsApiServlet(ClientService clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = clientService.getClient( extractIdFromRequest(request) ).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(client));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = gson.fromJson( request.getReader(), Client.class );
        clientService.saveClient( client );

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(client));
    }

}
