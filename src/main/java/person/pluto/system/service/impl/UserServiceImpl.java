package person.pluto.system.service.impl;

import person.pluto.system.entity.User;
import person.pluto.system.repository.UserRepository;
import person.pluto.system.service.IUserService;

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
