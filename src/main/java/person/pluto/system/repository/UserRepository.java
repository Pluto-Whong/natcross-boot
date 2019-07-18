package person.pluto.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import person.pluto.system.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

}
