package uz.dilmurod.apphrmanagment.component;

import uz.dilmurod.apphrmanagment.entity.Company;
import uz.dilmurod.apphrmanagment.entity.Role;
import uz.dilmurod.apphrmanagment.entity.User;
import uz.dilmurod.apphrmanagment.repository.CompanyRepository;
import uz.dilmurod.apphrmanagment.repository.RoleRepository;
import uz.dilmurod.apphrmanagment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    final
    UserRepository userRepository;

    final
    RoleRepository roleRepository;

    final
    PasswordEncoder passwordEncoder;

    final
    CompanyRepository companyRepository;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) {
        if (initialMode.equals("always")) {

            Set<Role> roles = new HashSet<>(roleRepository.findAll());

            User user = new User("Director", passwordEncoder.encode("123"), roles, "dddd@gmail.com", "Director", true);

            User director = userRepository.save(user);

            Company company = new Company(director, "Google");
            companyRepository.save(company);
        }
    }
}
