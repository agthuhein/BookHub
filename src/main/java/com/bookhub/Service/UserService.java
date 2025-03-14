package com.bookhub.Service;

import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.CustomException.UserNotFoundException;
import com.bookhub.Model.Users;
import com.bookhub.Repository.UserRepository;
import com.bookhub.Security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //User can update only email, password, and phone number
    public boolean updateUser(Integer userId, Users updatedUser) {
        try{
            String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Users> loggedInUser = userRepository.findByEmail(loggedInEmail);

            System.out.println(loggedInUser);

            if(loggedInUser.isEmpty()){
                throw new UnauthorizedActionException("Unauthorized: User not found.");
            }

            Users l_user = loggedInUser.get();
            Optional<Users> toUpdate = userRepository.findById(userId);
            if(toUpdate.isEmpty()){
                throw new UsernameNotFoundException("User id: " + userId + " not found.");
            }

            Users toUpdateUser = toUpdate.get();
            // If logged-in user has 'USER' role, prevent them from updating an 'ADMIN' user
            if("USER".equals(l_user.getRole()) &&
                    "ADMIN".equals(toUpdateUser.getRole())){
                throw new UnauthorizedActionException("Unauthorized: You cannot update ADMIN user's information.");
            }
            // Proceed with the update
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                toUpdateUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                toUpdateUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().isEmpty()) {
                toUpdateUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }

            userRepository.save(toUpdateUser);
            return true;

        }
        catch (Exception e){
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    }
