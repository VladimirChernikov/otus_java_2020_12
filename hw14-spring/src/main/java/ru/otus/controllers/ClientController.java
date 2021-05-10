package ru.otus.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.otus.domain.Client;
import ru.otus.services.ClientService;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String mainView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @RequestMapping({"/client/refresh"})
    public String clientsRefreshView(@RequestParam(value="itemCount", required = true) Integer itemCount, Model model) {
        List<Client> clients = clientService.findLatest( itemCount == null ? 100 : itemCount );
        model.addAttribute("clients", clients);
        return "clients";
    }

    @RequestMapping({"/client/refreshPartial"})
    public String clientsRefreshPartialView(Model model) {
        List<Client> clients = clientService.findLatest( 1000 );
        model.addAttribute("clients", clients);
        return "clients::list";
    }

}
