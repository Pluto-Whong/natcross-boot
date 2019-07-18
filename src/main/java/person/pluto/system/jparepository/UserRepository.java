package person.pluto.system.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import person.pluto.system.jpaentity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

}
