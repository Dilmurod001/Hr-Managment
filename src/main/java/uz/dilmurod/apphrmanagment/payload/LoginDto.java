package uz.dilmurod.apphrmanagment.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
