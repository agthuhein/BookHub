package com.bookhub.Security;

import com.bookhub.CustomException.CustomAccessDeniedHandler;
import com.bookhub.CustomException.JwtAuthenticationEntryPoint;
import com.bookhub.CustomException.UnauthorizedActionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig{

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter)
            throws Exception {
        http.csrf(csrf -> csrf.disable())
                .headers
                        (headers ->
                                headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/getAllAuthors").permitAll()
                        .requestMatchers("/getAllPublishers").permitAll()
                        .requestMatchers("/getAllCategories").permitAll()
                        .requestMatchers("/getAllBooks").permitAll()
                        .requestMatchers("/getBookByTitle",
                                "/getBookById/{bookId}",
                                "/getBookByISBN/{isbn}",
                                "/getBookByAuthor/{authorId}",
                                "/getBookByCategory/{categoryId}",
                                "/getBookByPublisher/{publisherId}",
                                "/getAllReviews", "/getReviewByBook/{bookId}").permitAll()
                        .requestMatchers("/api/users/**", "/addReview", "/updateReview/{reviewId}").hasRole("USER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/api/users/{userId}/update").permitAll()
                        .requestMatchers("/api/updateUser/{userId}", "/deleteReview/{reviewId}").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
