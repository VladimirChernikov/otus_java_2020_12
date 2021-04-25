package ru.otus.services;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.User;

public class UserAuthServiceImpl implements UserAuthService {
    private static final Logger log = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;
    private final Field loginField;

    public UserAuthServiceImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) throws NoSuchFieldException, SecurityException {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
        this.loginField = User.class.getDeclaredField("login");
    }

    @Override
    public boolean authenticate(String login, String password) {
        return transactionManager.doInTransaction(session -> {
            boolean result = false;
            List<User> users = userDataTemplate.findByField(session, this.loginField, login);
            for ( var user : users ) {
                if ( user.getPassword().equals(password) ) {
                    result = true;
                }
            }
            return result;
        });

    }

}
