package com.works.restcontrollers;

import com.works.entities.Customer;
import com.works.services.CustomerService;

import org.hibernate.validator.constraints.Length;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@RestController
@RequestMapping("/customer")

@Validated

public class CustomerRestController {
    final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return customerService.register(customer);
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam @NotBlank(message = "oldPassword can not be blank") String oldPassword, @RequestParam @NotBlank(message = "password can not be blank") @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
            regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})") String newPassword){
      return  customerService.changePassword(oldPassword,newPassword);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return customerService.delete(id);
    }

    @PutMapping("/settings")
    public ResponseEntity settings( @RequestParam  @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50) String firstName, @RequestParam @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50)  String secondName, @RequestParam @Email(message = "E-mail Format error") String email, @RequestParam  @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50) String telephone ){
        return customerService.settings(firstName, secondName,email,telephone);
    }

    @PutMapping("/changeCustomerEnable")
    public ResponseEntity changeEnable(@RequestParam Long id,@RequestParam boolean enable){
        return customerService.changeEnableCustomer(id,enable);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return customerService.list();
    }

}
