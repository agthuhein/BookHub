package com.bookhub.Controller;

import com.bookhub.Model.Users;
import com.bookhub.Security.JwtUtil;
import com.bookhub.Service.AuthService;
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
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private AuthService userService;

    public AuthController(AuthService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody Users user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }

        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user. " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public String login(@RequestBody Users user)
        throws AuthenticationException {
       try{
           String userEmail = user.getEmail();
           String password = user.getPassword();

           Authentication auth = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(userEmail, password)
           );
           if(auth.isAuthenticated()) {
               return jwtUtil.generateToken(userEmail);
           }
           else {
               throw new BadCredentialsException("Invalid credentials");
           }
       }
       catch (BadCredentialsException e) {
           return "Error: " + e.getMessage();
       }
    }

}
