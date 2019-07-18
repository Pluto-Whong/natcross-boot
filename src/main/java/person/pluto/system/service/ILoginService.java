package person.pluto.system.service;

import person.pluto.system.model.LoginResult;

public interface ILoginService {
    LoginResult login(String userName, String password);

    void logout();
}
