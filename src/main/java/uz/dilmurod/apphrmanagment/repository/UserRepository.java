package uz.dilmurod.apphrmanagment.repository;

import uz.dilmurod.apphrmanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(@Email String email);
    boolean existsByEmail(@Email String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
