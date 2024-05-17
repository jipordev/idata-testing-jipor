package co.istad.idata.features.user;

import co.istad.idata.domains.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
