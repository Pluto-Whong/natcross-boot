package person.pluto.system.jpaservice;

import person.pluto.system.jpaentity.User;

public interface IUserService {

    User findByUserName(String userName);

}
