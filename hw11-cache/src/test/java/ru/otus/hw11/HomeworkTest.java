package ru.otus.hw11;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.ref.WeakReference;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionManagerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.hw09.jdbc.mapper.DataTemplateJdbc;
import ru.otus.hw09.jdbc.mapper.EntityMetaData;
import ru.otus.hw09.jdbc.mapper.EntityMetaDataImpl;
import ru.otus.hw09.jdbc.mapper.EntitySQLMetaData;
import ru.otus.hw09.jdbc.mapper.EntitySQLMetaDataImpl;
import ru.otus.hw11.cachehw.MyCache;
import ru.otus.hw11.crm.service.DbServiceClientCachedImpl;

public class HomeworkTest {

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeworkTest.class);

    @BeforeAll
    public static void init() {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        HomeworkTest.flywayMigrations(dataSource);
    }

    @AfterAll
    public static void shutdown() {

    }

    @DisplayName("Кэш работает быстрее СУБД")
    @Test
    public void shouldBeFasterWithCache() {
        //given
        final double fasterPrc = 0.8;
        final int countOfRecords = 100;
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        var transactionManager = new TransactionManagerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityMetaData entityMetaDataClient = new EntityMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl();
        var dataTemplateClient = new DataTemplateJdbc<Client>(entityMetaDataClient, entitySQLMetaDataClient, dbExecutor);

        var myCache = new MyCache<String, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionManager, dataTemplateClient);
        var dbServiceClientCached = new DbServiceClientCachedImpl(dbServiceClient, myCache);

        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            dbServiceClientCached.saveClient(new Client(idx, "Client #"+idx));
        }

        //when
        long getClientCacheTime = System.currentTimeMillis();
        long cacheChecksum = 0;
        long cacheCount = 0;
        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            Client client = dbServiceClientCached.getClient(idx, true).orElse(null);
            if ( client != null ) {
                cacheChecksum += client.toString().hashCode();
                cacheCount++;
            }
        }
        getClientCacheTime = System.currentTimeMillis() - getClientCacheTime;

        dbServiceClientCached.disableCache();

        long getClientDbTime = System.currentTimeMillis();
        long dbChecksum = 0;
        long dbCount = 0;
        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            Client client = dbServiceClientCached.getClient(idx).orElse(null);
            if ( client != null ) {
                dbChecksum += client.toString().hashCode();
                dbCount++;
            }
        }
        getClientDbTime = System.currentTimeMillis() - getClientDbTime;

        assertTrue( cacheCount == dbCount );
        assertTrue( dbCount == countOfRecords );
        assertTrue( dbChecksum == cacheChecksum );
        
        //then
        assertTrue( getClientCacheTime < getClientDbTime*fasterPrc );

    }

    @DisplayName("Кэш сбрасывается при недостатке памяти")
    @Test
    public void shouldBePurgedOnGc() {
        //given
        final int countOfRecords = 100;
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        var transactionManager = new TransactionManagerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityMetaData entityMetaDataClient = new EntityMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl();
        var dataTemplateClient = new DataTemplateJdbc<Client>(entityMetaDataClient, entitySQLMetaDataClient, dbExecutor);

        var myCache = new MyCache<String, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionManager, dataTemplateClient);
        var dbServiceClientCached = new DbServiceClientCachedImpl(dbServiceClient, myCache);

        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            dbServiceClientCached.saveClient(new Client(idx, "Client #"+idx));
        }

        //when
        forceGc();
       
        long cacheCount = 0;
        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            Client client = dbServiceClientCached.getClient(idx, true).orElse(null);
            if ( client != null ) {
                cacheCount++;
            }
        }

        dbServiceClientCached.disableCache();

        long getClientDbTime = System.currentTimeMillis();
        long dbCount = 0;
        for ( long idx = 1; idx <= countOfRecords; idx++ ) {
            Client client = dbServiceClientCached.getClient(idx).orElse(null);
            if ( client != null ) {
                dbCount++;
            }
        }
        getClientDbTime = System.currentTimeMillis() - getClientDbTime;

        assertTrue( dbCount != cacheCount );
        
        //then
        assertTrue( cacheCount == 0 );

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }

    private static void forceGc() {
        Object obj = new Object();
        WeakReference<Object> ref = new WeakReference<Object>(obj);
        obj = null;
        while ( ref.get() != null ) {
            System.gc();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
