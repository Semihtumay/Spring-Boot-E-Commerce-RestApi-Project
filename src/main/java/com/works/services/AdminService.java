package com.works.services;

import com.works.entities.Admin;
import com.works.repositories.AdminRepository;
import com.works.repositories.RoleRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class AdminService {
  final AdminRepository adminRepository;
  final RoleRepository roleRepository;
  final PasswordEncoder passwordEncoder;
  final JavaMailSender javaMailSender;
  final HttpSession session;

    public AdminService(AdminRepository adminRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, HttpSession session) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.session = session;
    }

    public ResponseEntity register(Admin admin){
        HashMap<REnum,Object> hm=new LinkedHashMap<>();
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        if(optionalAdmin.isPresent()){
            hm.put(REnum.status, false);
            hm.put(REnum.message,"This admin have already registered");
            return new ResponseEntity( hm, HttpStatus.NOT_ACCEPTABLE );
        }else{
            admin.setFirstName(admin.getFirstName());
            admin.setLastName(admin.getLastName());
            admin.setCompanyName(admin.getCompanyName());
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin adminNew=adminRepository.save(admin);
            hm.put(REnum.status,true);
            hm.put(REnum.result,adminNew);
            return new ResponseEntity<>(hm,HttpStatus.OK);

        }

    }

    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        Map<REnum, Object> hm = new LinkedHashMap();

        Admin admin= (Admin) session.getAttribute("admin");

        if (this.passwordEncoder.matches(oldPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            Admin updatedAdmin = adminRepository.save(admin);
            hm.put(REnum.status, "true");
            hm.put(REnum.result, updatedAdmin);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.message, "Please check again current password");
            hm.put(REnum.status, "false");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity settings(String companyName, String firstName, String lastName, String email, String phone ) {

        Map<REnum, Object> hm = new LinkedHashMap();
        try {

            Admin oldAdmin= (Admin) session.getAttribute("admin");
            Optional<Admin> admin_ByEmail = adminRepository.findByEmailEqualsIgnoreCase(email);

            if ((oldAdmin.getEmail().equals(email)) || !admin_ByEmail.isPresent()) {
                    oldAdmin.setCompanyName(companyName);
                    oldAdmin.setFirstName(firstName);
                    oldAdmin.setLastName(lastName);
                    oldAdmin.setEmail(email);
                    oldAdmin.setPhone(phone);

                    adminRepository.saveAndFlush(oldAdmin);
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, oldAdmin);
                    return new ResponseEntity<>(hm, HttpStatus.OK);
                } else {
                    hm.put(REnum.status, false);
                    hm.put(REnum.message, "This email already registered");
                    return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
                }
        } catch (Exception exception) {
            hm.put(REnum.status, false);
            hm.put(REnum.message,exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

}
