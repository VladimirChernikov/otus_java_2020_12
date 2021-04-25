package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.jetty.server.handler.ErrorHandler;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.model.User;
import ru.otus.serializers.GsonClientDeserializer;
import ru.otus.serializers.GsonClientSerializer;
import ru.otus.server.CrmErrorHandler;
import ru.otus.server.CrmWebServer;
import ru.otus.server.CrmWebServerWithFilterBasedSecurity;
import ru.otus.services.ClientService;
import ru.otus.services.ClientServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

/*
    // Стартовая страница
    http://localhost:8080
*/
public class CrmWebConsole {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String TEMPLATES_DIR = "/templates/";
    private static final int WEB_SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        // Repository
        Configuration hibernateConf = new Configuration().configure(HIBERNATE_CFG_FILE);
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(hibernateConf, User.class, Client.class, Phone.class, Address.class);
        TransactionManagerHibernate transactionManager = new TransactionManagerHibernate(sessionFactory);

        // Services
        UserAuthService authService = new UserAuthServiceImpl( transactionManager, new DataTemplateHibernate<User>(User.class) );
        // LoginService loginService = new UserLoginService(transactionManager, new DataTemplateHibernate<User>(User.class) );
        ClientService clientService = new ClientServiceImpl( transactionManager, new DataTemplateHibernate<Client>(Client.class) );

        // Web view
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        // Util
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Client.class, new GsonClientDeserializer())
            .registerTypeAdapter(Client.class, new GsonClientSerializer())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

        ErrorHandler errorHandler = new CrmErrorHandler();

        // Web server
        CrmWebServer crmWebServer = new CrmWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, clientService, gson, templateProcessor, errorHandler);
        // CrmWebServer crmWebServer = new CrmWebServerWithBasicSecurity(WEB_SERVER_PORT,
        //         loginService, clientService, gson, templateProcessor, errorHandler);
        crmWebServer.start();
        crmWebServer.join();
    }
}
