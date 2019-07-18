package person.pluto.system.jpaservice;

import person.pluto.system.model.LoginResult;

public interface ILoginService {
    LoginResult login(String userName, String password);

    void logout();
}
