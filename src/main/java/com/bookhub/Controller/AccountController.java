package com.bookhub.Controller;

import com.bookhub.CustomException.MethodArgumentNotValidException;
import com.bookhub.Model.Users;
import com.bookhub.Security.JwtUtil;
import com.bookhub.Service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private AccountService authService;

    public AccountController(AccountService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody Users user, BindingResult result) {
        if (result.hasErrors()) {
            //throw new MethodArgumentNotValidException("Empty variables. Enter necessary fields.");
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data: " + result.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data." + result.getAllErrors());
        }

        try {
            authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user. " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Users user) {
        try {
            String userEmail = user.getEmail();
            String password = user.getPassword();
            String role = user.getRole();

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, password)
            );

            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(userEmail, role);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }


}
