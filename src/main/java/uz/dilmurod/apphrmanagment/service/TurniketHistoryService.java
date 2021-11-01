package uz.dilmurod.apphrmanagment.service;

import uz.dilmurod.apphrmanagment.component.Checker;
import uz.dilmurod.apphrmanagment.entity.Role;
import uz.dilmurod.apphrmanagment.entity.Tourniquet;
import uz.dilmurod.apphrmanagment.entity.TurniketHistory;
import uz.dilmurod.apphrmanagment.enums.RoleName;
import uz.dilmurod.apphrmanagment.payload.TurniketHistoryDto;
import uz.dilmurod.apphrmanagment.payload.response.ApiResponse;
import uz.dilmurod.apphrmanagment.repository.TurniketHistoryRepository;
import uz.dilmurod.apphrmanagment.repository.TurniketRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketHistoryService {

    final
    TurniketHistoryRepository turniketHistoryRepository;

    final
    TurniketRepository turniketRepository;

    final
    Checker checker;

    public TurniketHistoryService(TurniketHistoryRepository turniketHistoryRepository, TurniketRepository turniketRepository, Checker checker) {
        this.turniketHistoryRepository = turniketHistoryRepository;
        this.turniketRepository = turniketRepository;
        this.checker = checker;
    }

    public ApiResponse add(TurniketHistoryDto turniketHistoryDto){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(turniketHistoryDto.getNumber());
        if (!optionalTurniket.isPresent()) {
            return new ApiResponse("tourniquet not found!", false);
        }

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found!", false);

        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(optionalTurniket.get());
        turniketHistory.setType(turniketHistoryDto.getType());
        turniketHistoryRepository.save(turniketHistory);
        return new ApiResponse("Success!", true);
    }

    public ApiResponse getAllByDate(String number, Timestamp startTime, Timestamp endTime){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("tourniquet not found", false);

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found", false);

        List<TurniketHistory> historyList = turniketHistoryRepository.findAllByTurniketAndTimeIsBetween(optionalTurniket.get(), startTime, endTime);
        return new ApiResponse("History list by date",true, historyList);
    }

    public ApiResponse getAll(String number){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("not found", false);

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found", false);

        List<TurniketHistory> allByTurniket = turniketHistoryRepository.findAllByTurniket(optionalTurniket.get());
        return new ApiResponse("All history by tourniquet!", true, allByTurniket);
    }
}
