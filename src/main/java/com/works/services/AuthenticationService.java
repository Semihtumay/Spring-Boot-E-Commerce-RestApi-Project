package com.works.services;

import com.works.configs.JwtUtil;
import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.entities.Role;
import com.works.repositories.AdminRepository;
import com.works.repositories.CustomerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationService implements UserDetailsService {

    final JwtUtil jwtUtil;
    final CustomerRepository customerRepository;
    final AdminRepository adminRepository;
    final AuthenticationManager authenticationManager;
    final HttpSession session;

    public AuthenticationService(JwtUtil jwtUtil, CustomerRepository customerRepository, AdminRepository adminRepository, @Lazy AuthenticationManager authenticationManager, HttpSession session) {
        this.jwtUtil = jwtUtil;
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.authenticationManager = authenticationManager;
        this.session = session;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(username);

        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(username);


        if(optionalCustomer.isPresent()&&!optionalAdmin.isPresent()){
            Customer customer=optionalCustomer.get();
            UserDetails userDetails = new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.isEnabled(),
                    customer.isTokenExpired(),
                    true,
                    true,
                    roles(customer.getRoles())

            );

            session.setAttribute("customer",customer);
            return userDetails;


        }else if(optionalAdmin.isPresent()|| optionalCustomer.isPresent()) {
            Admin admin=optionalAdmin.get();
            UserDetails userDetails = new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.isEnabled(),
                    admin.isTokenExpired(),
                    true,
                    true,
                    roles(admin.getRoles())

            );
            session.setAttribute("admin",admin);
            return userDetails;
        }else {throw new UsernameNotFoundException("There is no such user. ");}

    }
    public Collection roles(List<Role> roles ) {
        List<GrantedAuthority> ls = new ArrayList<>();
        for ( Role role : roles ) {
            ls.add( new SimpleGrantedAuthority( role.getName() ));
        }
        System.out.println(ls);
        return ls;

    }
}
