package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.models.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByEmail(String email);

    Page<User> findByRole(Pageable pageable,UserRole role);

    Page<User> findByRoleAndNameContainsIgnoreCase(Pageable pageable, UserRole role, String name);

}
