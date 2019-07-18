package person.pluto.system.jpaservice.impl;

import person.pluto.system.jpaentity.User;
import person.pluto.system.jparepository.UserRepository;
import person.pluto.system.jpaservice.IUserService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
