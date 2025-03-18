package com.bookhub.Service;

import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.CustomException.UserNotFoundException;
import com.bookhub.Model.Users;
import com.bookhub.Repository.MySQL.UsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;
    public UserService(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //User can update only email, password, and phone number
    public boolean updateUser(Integer userId, Users updatedUser) {
        try {
            //Login user
            String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Users> loggedInUser = userRepository.findByEmail(loggedInEmail);

            //To update user
            Optional<Users> toUpdateUser = userRepository.findById(userId);

            if(loggedInUser.isEmpty()) {
                throw new UnauthorizedActionException("Unauthorized action. Please log in again.");
            }
            if(toUpdateUser.isEmpty()){
                throw new UserNotFoundException("User id: " + toUpdateUser.get().getUserId() + " not found");
            }

            Integer loggedInUserId = loggedInUser.get().getUserId();
            Integer toUpdateUserId = toUpdateUser.get().getUserId();

            String loginUserRole = loggedInUser.get().getRole();
            String toUpdateUserRole = toUpdateUser.get().getRole();

            if(loginUserRole.equals("ADMIN")){
                return userInfoUpdate(updatedUser, toUpdateUser);
            }
            if (loginUserRole.equals("USER") && toUpdateUserRole.equals("USER")) {
                if (!Objects.equals(loggedInUserId, toUpdateUserId)) {
                    throw new UnauthorizedActionException("Unauthorized action. Logged-in ID does not match.");
                }
                return userInfoUpdate(updatedUser, toUpdateUser);
            }
            throw new UnauthorizedActionException("Unauthorized action. You do not have permission to update ADMIN user.");
        }
        catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
        //return false;
    }

    private boolean userInfoUpdate(Users updatedUser, Optional<Users> toUpdateUser) {
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            toUpdateUser.get().setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(updatedUser.getPassword());
            //user.setPassword(encryptedPassword);
            toUpdateUser.get().setPassword(encryptedPassword);
        }
        if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().isEmpty()) {
            toUpdateUser.get().setPhoneNumber(updatedUser.getPhoneNumber());
        }
        userRepository.save(toUpdateUser.get());
        return true;
    }

    public Integer getUserId(String userEmail) {
        if (userEmail != null && !userEmail.isEmpty()) {
            try{
                Optional<Users> user = userRepository.findByEmail(userEmail);
                if (user.isPresent()) {
                    return user.get().getUserId();
                }
            }
            catch (Exception e) {
                throw new UserNotFoundException("No related user with this email: " + userEmail);
            }
        }
        return null;
    }
}
