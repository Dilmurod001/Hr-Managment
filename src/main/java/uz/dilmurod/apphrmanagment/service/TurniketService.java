package uz.dilmurod.apphrmanagment.service;

import uz.dilmurod.apphrmanagment.component.Checker;
import uz.dilmurod.apphrmanagment.component.MailSender;
import uz.dilmurod.apphrmanagment.entity.Company;
import uz.dilmurod.apphrmanagment.entity.Role;
import uz.dilmurod.apphrmanagment.entity.Tourniquet;
import uz.dilmurod.apphrmanagment.entity.User;
import uz.dilmurod.apphrmanagment.enums.RoleName;
import uz.dilmurod.apphrmanagment.payload.TurniketDto;
import uz.dilmurod.apphrmanagment.payload.response.ApiResponse;
import uz.dilmurod.apphrmanagment.repository.CompanyRepository;
import uz.dilmurod.apphrmanagment.repository.TurniketRepository;
import uz.dilmurod.apphrmanagment.security.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketService {

    final
    TurniketRepository turniketRepository;

    final
    Checker checker;

    final
    UserService userService;

    final
    CompanyRepository companyRepository;

    final
    MailSender mailSender;

    final
    JwtProvider jwtProvider;

    public TurniketService(TurniketRepository turniketRepository, Checker checker, UserService userService, CompanyRepository companyRepository, MailSender mailSender, JwtProvider jwtProvider) {
        this.turniketRepository = turniketRepository;
        this.checker = checker;
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.mailSender = mailSender;
        this.jwtProvider = jwtProvider;
    }

    public ApiResponse add(TurniketDto turniketDto) throws MessagingException {
        ApiResponse response = userService.getByEmail(turniketDto.getOwnerEmail());
        if (!response.isStatus())
            return response;

        User user = (User) response.getObject();
        Optional<Company> optionalCompany = companyRepository.findById(turniketDto.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ApiResponse("Company not found!", false);

        Tourniquet turniket = new Tourniquet();
        turniket.setCompany(optionalCompany.get());
        turniket.setOwner(user);
        assert !turniketDto.isEnabled();
        turniket.setEnabled(turniketDto.isEnabled());
        Tourniquet saved = turniketRepository.save(turniket);
        mailSender.mailTextTurniketStatus(saved.getOwner().getEmail(), saved.isEnabled());
        return new ApiResponse("Turniket succesfully created!", true);
    }

    public ApiResponse edit(String number, TurniketDto turniketDto) throws MessagingException {
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("tourniquet not found!", false);

        Tourniquet turniket = optionalTurniket.get();
        turniket.setEnabled(turniketDto.isEnabled());
        Tourniquet saved = turniketRepository.save(turniket);
        mailSender.mailTextTurniketStatus(saved.getOwner().getEmail(), saved.isEnabled());
        return new ApiResponse("tourniquet successfully edited!", true);
    }

    public ApiResponse delete(String number){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
                return new ApiResponse("tourniquet not found!", false);


        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = null;
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }
        boolean check = checker.check(role);

        if (!check)
            return new ApiResponse("You have no such right!", false);

        turniketRepository.delete(optionalTurniket.get());
        return new ApiResponse("tourniquet deleted!", true);
    }

    public ApiResponse getByNumber(String number){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse byEmail = userService.getByEmailforCustom(user.getEmail());
        if (!byEmail.isStatus())
            return byEmail;

        Optional<Tourniquet> byNumber = turniketRepository.findByNumber(number);
        if (!byNumber.isPresent())
            return new ApiResponse("tourniquet not found!", false);

        Set<Role> roles = byNumber.get().getOwner().getRoles();
        String role = null;
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }
        boolean check = checker.check(role);

        if (byNumber.get().getOwner().getEmail().equals(user.getEmail()) || check){
            return new ApiResponse("tourniquet", true, byNumber.get());
        }
        return new ApiResponse("You have no such right!", false);
    }

    //by Token
    public ApiResponse getAll(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse byEmail = userService.getByEmailforCustom(user.getEmail());
        if (!byEmail.isStatus())
            return byEmail;

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }

        if (role.equals(RoleName.ROLE_DIRECTOR.name()))
            return new ApiResponse("tourniquet List",true, turniketRepository.findAll());

        return new ApiResponse("tourniquet List",true, turniketRepository.findAllByOwner(user));
    }

    public ApiResponse getByUser(User user){
        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }
        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("forbidden!", false);

        Optional<Tourniquet> optionalTurniket = turniketRepository.findAllByOwner(user);
        return new ApiResponse("tourniquetListByUser", true, optionalTurniket);
    }
}
