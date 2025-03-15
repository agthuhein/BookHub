package com.bookhub.Security;

import com.bookhub.Service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //Getting authorization header from the request
        final String authorizationHeader = request.getHeader("Authorization");

        String userEmail = null;
        String jwtToken = null;

        //Checking whether authorization header contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try{
                userEmail = jwtUtil.extractUserEmail(jwtToken);
            }
            catch (ExpiredJwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");

            } catch (UnsupportedJwtException | MalformedJwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");

            }
            catch(JwtException ex){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT validation failed");

            }
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try{
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if(jwtUtil.validateToken(jwtToken, userDetails.getUsername())){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            catch (UsernameNotFoundException e)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            }

        }
        filterChain.doFilter(request, response);
    }
}

