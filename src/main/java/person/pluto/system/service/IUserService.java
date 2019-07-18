package person.pluto.system.service;

import person.pluto.system.entity.User;

public interface IUserService {

    User findByUserName(String userName);

}
