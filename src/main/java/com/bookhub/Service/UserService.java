package com.bookhub.Service;

import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.CustomException.UserNotFoundException;
import com.bookhub.Model.Users;
import com.bookhub.Repository.MySQL.UsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


            System.out.println(loggedInUser);
            System.out.println(toUpdateUser);
            if(loggedInUser.isEmpty()) {
                throw new UnauthorizedActionException("Unauthorized action. Please log in again.");
            }
            if(toUpdateUser.isEmpty()){
                throw new UserNotFoundException("User id: " + toUpdateUser.get().getUserId() + " not found");
            }
            String loginUserRole = loggedInUser.get().getRole();
            String toUpdateUserRole = toUpdateUser.get().getRole();

            switch (loginUserRole) {
                case "USER":
                    if (toUpdateUserRole.equals("USER")) {
                        return userInfoUpdate(updatedUser, toUpdateUser);
                    } else {
                        throw new UnauthorizedActionException("Unauthorized action to update an ADMIN's information");
                    }
                case "ADMIN":
                    if (toUpdateUserRole.equals("ADMIN") || toUpdateUserRole.equals("USER")) {
                        return userInfoUpdate(updatedUser, toUpdateUser);
                    }
                    break;
                default:
                    throw new UnauthorizedActionException("Unauthorized action to update an ADMIN's information");
            }
        }
        catch (UnauthorizedActionException e) {
            throw new UnauthorizedActionException("Unauthorized action to update an ADMIN's information");
        }
        catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
        return false;
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
}
