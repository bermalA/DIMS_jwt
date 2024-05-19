package jwt.dimsjwt.repository;

import jwt.dimsjwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT u FROM User u WHERE u.username = :uname")
    User getReferenceByUname(String uname);
}
