package person.pluto.system.jpaservice;

import person.pluto.system.model.LoginResult;

public interface IJpaLoginService {
    LoginResult login(String userName, String password);

    void logout();
}
