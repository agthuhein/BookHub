package com.bookhub.Service;

import com.bookhub.Model.Users;
import com.bookhub.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + users.getRole());
        //System.out.println(users.getRole());
        return org.springframework.security.core.userdetails.User
                .withUsername(users.getEmail())
                .password(users.getPassword())
                .roles(users.getRole())
                .build();
    }
}
