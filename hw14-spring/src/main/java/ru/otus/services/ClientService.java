package ru.otus.services;

import ru.otus.domain.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Client findById(long id);

    List<Client> findByName(String name);

    Client save(Client client);

    List<Client> findLatest(int count);

}
