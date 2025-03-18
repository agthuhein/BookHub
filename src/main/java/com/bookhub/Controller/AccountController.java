package com.bookhub.Controller;

import com.bookhub.CustomException.MethodArgumentNotValidException;
import com.bookhub.Model.Users;
import com.bookhub.Security.JwtUtil;
import com.bookhub.Service.AccountService;
import com.bookhub.Service.UserService;
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

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private AccountService authService;
    private final UserService userService;

    public AccountController(AccountService authService, AuthenticationManager authenticationManager,
                             UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody Users user, BindingResult result) {
        if (result.hasErrors()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", result.getFieldErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList())));
        }

        try {
            authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Users user) {
        try {
            String userEmail = user.getEmail();
            String password = user.getPassword();

            Integer userID = userService.getUserId(userEmail);
            //System.out.println("ID " + userID);


            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, password)
            );

            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(userEmail, userID);
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
