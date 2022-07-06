package com.works.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.works.utils.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class Admin extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(message = "Your company name cannot exceed 50 characters.", max = 50)
    @NotBlank(message = "Please Enter Company Name")
    private String companyName;
    @Length(message = "Your first name cannot exceed 50 characters.", max = 50)
    @NotBlank(message = "Please Enter First Name")
    private String firstName;
    @NotBlank(message = "Please Enter Last Name")
    @Length(message = "Your last  name cannot exceed 50 characters.", min = 1, max = 50)
    private String lastName;
    @Length(message = "Your phone number cannot exceed 50 characters.", min = 5, max = 50)
    @NotBlank(message = "Please Enter Phone Number")
    private String phone;
    @Length(message = "Your e-mail cannot exceed 50 characters.", max = 60)
    @NotBlank(message = "Please Enter E-Mail")
    @Email(message = "E-mail Format Exception")
    private String email;
    @ValidPassword
    @NotBlank(message = "Please Enter Password")
    private String password;
    private boolean enabled;
    private boolean tokenExpired;
    private String verificationCode;



@ManyToMany(fetch = FetchType.EAGER)
@JoinTable( name = "admin_role",
        joinColumns = @JoinColumn( name = "admin_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id")
)
private List<Role> roles;





}
