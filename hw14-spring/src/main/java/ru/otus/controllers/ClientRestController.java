package ru.otus.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.otus.domain.Client;
import ru.otus.domain.exceptions.AppDomainException;
import ru.otus.services.ClientService;
import ru.otus.services.exceptions.AppServicesException;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.findById(id);
    }

    @GetMapping("/api/client")
    public List<Client> getClientByName(@RequestParam(name = "name") String name) {
        return clientService.findByName(name);
    }

    @PostMapping("/api/client")
    public Object saveClient(@RequestBody Client client) {
        try {
            return clientService.save(client).toString();
        } catch (AppDomainException | AppServicesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occured. Please contact your system administrator.");
        }
    }

}
