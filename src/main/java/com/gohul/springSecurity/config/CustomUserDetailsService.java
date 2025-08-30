package com.gohul.springSecurity.config;

import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.customer.CustomerRepo;
import com.gohul.springSecurity.customer.CustomerRoles;
import com.gohul.springSecurity.customer.RolesRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepo repo;
    private final RolesRepo cusRoleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found with email::" + username));
        List<CustomerRoles> rolesList = cusRoleRepo.findByCustomer(customer);
        var roles = rolesList.stream()
                .map(authZ -> new SimpleGrantedAuthority(authZ.getRole())).toList();
        return new User(customer.getEmail(), customer.getPassword(), roles);

    }
}
