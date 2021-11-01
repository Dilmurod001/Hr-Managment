package uz.dilmurod.apphrmanagment.payload;

import uz.dilmurod.apphrmanagment.enums.TurniketType;
import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class TurniketHistoryDto {
    @NotNull
    private String number;

    @NotNull
    @Enumerated
    private TurniketType type;
}
