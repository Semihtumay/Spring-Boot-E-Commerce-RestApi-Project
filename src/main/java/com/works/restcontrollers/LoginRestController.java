package com.works.restcontrollers;

import com.works.entities.Login;
import com.works.services.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class LoginRestController {

final PasswordService passwordService;

    public LoginRestController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
    @PostMapping("/login")
    public ResponseEntity auth(@RequestBody Login login) {
        return passwordService.auth(login);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam @Email(message = "E-mail Format Error") String email) {
        return passwordService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String verificationCode,@RequestParam @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
            regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})") String password){
        return passwordService.resetPassword(verificationCode,password);
    }
}
