package ru.otus.services;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.User;

public class UserLoginService extends AbstractLoginService {

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;
    private final Field loginField;

    public UserLoginService(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) throws NoSuchFieldException, SecurityException {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
        this.loginField = User.class.getDeclaredField("login");
    }


    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        return List.of(new RolePrincipal("user"));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        return transactionManager.doInTransaction(session -> {
            List<User> users = userDataTemplate.findByField(session, this.loginField, login);
            if ( users.size() > 0 )  {
                User user = users.get(0);
                return new UserPrincipal(user.getLogin(), new Password(user.getPassword()));
            }
            else  {
                return null;
            }
        });

    }
}
