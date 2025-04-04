package com.bookhub.Service;

import com.bookhub.Model.Users;
import com.bookhub.Repository.MySQL.UsersRepository;
import com.bookhub.Security.JwtUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AccountService(UsersRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public String registerUser(@RequestBody Users user) {
        // Check if the user already exists based on email
        Optional<Users> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use. Please try again with another email.");
        }

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());

        // Hash the password before saving it
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setEmail(user.getEmail());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setRole(user.getRole());

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        try {
            userRepository.save(user);
            String token = jwtUtil.generateToken(user.getEmail(), user.getUserId());
            return token;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Error saving user to the database", e);
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Database access error occurred while saving users.", e);
        }
        catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while saving users.", e);
        }
    }
}
