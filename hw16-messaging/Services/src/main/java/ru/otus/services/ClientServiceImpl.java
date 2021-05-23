package ru.otus.services;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.domain.Client;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.repository.ClientRepository;
import ru.otus.services.exceptions.AppServicesException;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> findLatest(int count) {
        Pageable pageable = PageRequest.of(0, count, Sort.by( Order.desc("id") ));

        Page<Client> clientPage = clientRepository.findAll(pageable);
        // Long clientTotalCount = clientRepository.count();

        return clientPage.getContent();
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> findByName(String name) {
        return clientRepository.findByName(name);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        try {
            if ( client.getId() == null ) {
                return clientRepository.save( client.toBuilder().isNew(true).build() );
            }
            else {
                return clientRepository.save( client.toBuilder().isNew(false).build() );
            }
        } catch (OptimisticLockingFailureException e){
            throw new AppServicesException( String.format("Record is outdated! Unable to save client = %s",client) );
        }
    }
}
