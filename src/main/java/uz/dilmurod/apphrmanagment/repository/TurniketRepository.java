package uz.dilmurod.apphrmanagment.repository;

import uz.dilmurod.apphrmanagment.entity.Tourniquet;
import uz.dilmurod.apphrmanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Tourniquet, UUID> {
    Optional<Tourniquet> findByNumber(String number);
    Optional<Tourniquet> findAllByOwner(User owner);
}
