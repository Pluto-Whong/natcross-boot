package person.pluto.system.jpaservice.impl;

import person.pluto.system.jpaentity.User;
import person.pluto.system.jparepository.UserRepository;
import person.pluto.system.jpaservice.IJpaUserService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JpaUserServiceImpl implements IJpaUserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
