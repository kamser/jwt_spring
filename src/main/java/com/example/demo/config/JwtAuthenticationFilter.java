package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//This class represents the first filter that triggers everytime a Request tries to get a resource from the server
//so this class has to extends the class OncePerRequestFilter to have a listener when a request arrives to the server.

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String  JWT_SECRET_WORD = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); //Extract the jwt from the request header.
        final String jwt;
        final String userEmail;
        //Going to do the first filter related with no token or a wrong content for the token
        if(authHeader == null || !authHeader.startsWith(JWT_SECRET_WORD)){
            filterChain.doFilter(request, response);
            return; // to stop the verification
        }

        //In case the token pass the first filter, then proceed to extract the user information to check if the user exist
        jwt = authHeader.substring(JWT_SECRET_WORD.length()); //The jwt start after the secret phrase of the header
        userEmail = jwtService.extractUsername(jwt); //Extract the email from the jwt using a created service to manipulate the jwt string to extract the content.

        //Now that I have the service to manipulate the token and extract the userEmail (or Username) I need to use that information to check if the user exist in the database.
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUserName(userEmail);
        }
    }
}
