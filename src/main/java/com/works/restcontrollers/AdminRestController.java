package com.works.restcontrollers;

import com.works.entities.Admin;
import com.works.entities.Login;
import com.works.services.AdminService;
import com.works.services.AuthenticationService;
import com.works.services.PasswordService;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminRestController {
     final AdminService adminService;
     final AuthenticationService authenticationService;
     final PasswordService passwordService;

    public AdminRestController(AdminService adminService, AuthenticationService authenticationService, PasswordService passwordService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.passwordService = passwordService;
    }


    @PostMapping("/register")
    public ResponseEntity registerAdmin(@Valid @RequestBody Admin admin) {
        return adminService.register(admin);
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@Valid @RequestBody Login login) {
        return passwordService.auth(login);
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam @NotBlank(message = "password can not be blank") String newPassword){
        return  adminService.changePassword(oldPassword,newPassword);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        return passwordService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam String password){
        return passwordService.resetPassword(resettoken,password);
    }

    @PutMapping("/settings")
    public ResponseEntity settings(@RequestParam @Length (message = "companyName  must contain min 2 max  50 character.", min = 2, max = 50) String companyName,
                                   @RequestParam  @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50) String firstName,
                                   @RequestParam @Length(message = "lastName  must contain min 2 max  50 character.", min = 2, max = 50)  String lastName,
                                   @RequestParam @Email(message = "E-mail Format error") String email,
                                   @RequestParam  @Length(message = "phone must contain min 10 max  5O character.", min = 10, max = 50) String phone ){
        return adminService.settings(companyName, firstName, lastName, email, phone);
    }
}
