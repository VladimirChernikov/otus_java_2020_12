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
    private final HwCache<String, Client> cache;
    private boolean useCache;

    public DbServiceClientCachedImpl(DbServiceClientImpl dbServiceClient, HwCache<String, Client> cache ) {
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
            this.cacheClient( client );
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
            client = this.cache.get(String.valueOf(id));
        }
        if ( client == null && !cacheOnly ) {
            Optional<Client> clientOptional = this.dbServiceClient.getClient(id);
            if ( clientOptional.isPresent() ) {
                this.cacheClient( clientOptional.get() );
            }
            return clientOptional;
        }
        else {
            return Optional.ofNullable(client);
        }
    }

    @Override
    public List<Client> findAll() {
        return this.dbServiceClient.findAll();
    }

    private void cacheClient( Client client ) {
        this.cache.put( String.valueOf( client.getId() ), client.clone() );
    }

}
