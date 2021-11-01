package uz.dilmurod.apphrmanagment.controller;

import uz.dilmurod.apphrmanagment.payload.LoginDto;
import uz.dilmurod.apphrmanagment.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public HttpEntity<?> logIntoSystem(@RequestBody LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            String token = jwtProvider.generateToken(loginDto.getEmail());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("Login and password incorrect:)");
        }
    }
}
