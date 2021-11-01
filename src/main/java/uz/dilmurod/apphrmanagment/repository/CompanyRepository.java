package uz.dilmurod.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dilmurod.apphrmanagment.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
