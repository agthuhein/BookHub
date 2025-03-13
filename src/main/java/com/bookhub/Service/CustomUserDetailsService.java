package com.bookhub.Service;

import com.bookhub.Model.Users;
import com.bookhub.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

//        return org.springframework.security.core.userdetails.User.builder()
//                .username(users.getFirstName() + " " + users.getLastName())
//                .password(users.getPassword())
//                .roles(users.getRole())
//                .build();
        return org.springframework.security.core.userdetails.User
                .withUsername(users.getEmail())  // Use email instead of username
                .password(users.getPassword())   // Password should be encoded
                .roles(users.getRole())          // Adjust roles accordingly
                .build();
    }
}
