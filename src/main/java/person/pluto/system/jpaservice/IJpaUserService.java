package person.pluto.system.jpaservice;

import person.pluto.system.jpaentity.User;

public interface IJpaUserService {

    User findByUserName(String userName);

}
