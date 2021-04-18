package ru.otus.hw11.crm.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.hw11.cachehw.HwCache;

public class DbServiceClientCachedImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientCachedImpl.class);

    private final DbServiceClientImpl dbServiceClient;
    private final HwCache<Long, Client> cache;
    private boolean useCache;

    public DbServiceClientCachedImpl(DbServiceClientImpl dbServiceClient, HwCache<Long, Client> cache ) {
        this.dbServiceClient = dbServiceClient;
        this.useCache = true;
        this.cache = cache;
    }

    public void disableCache() {
        this.useCache = false;
    }

    public void enableCache() {
        this.useCache = true;;
    }

    public boolean isUsingCache() {
        return this.useCache;
    }

    @Override
    public Client saveClient(Client client) {
        if ( this.useCache ) {
            this.cache.put( new Long( client.getId() ), client.clone() );
        }
        return this.dbServiceClient.saveClient(client.clone());
    }

    @Override
    public Optional<Client> getClient(long id) {
        return this.getClient( id, false );
    }

    public Optional<Client> getClient(long id, boolean cacheOnly) {
        Client client = null;
        if ( this.useCache ) {
            client = this.cache.get(id);
        }
        if ( client == null && !cacheOnly ) {
            return this.dbServiceClient.getClient(id);
        }
        else {
            return Optional.ofNullable(client);
        }
    }

    @Override
    public List<Client> findAll() {
        return this.dbServiceClient.findAll();
    }

}
