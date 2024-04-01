package com.example.demo.config;

import org.springframework.stereotype.Service;


/*
* This is the bean that going to manipulate the jwt, to do that, I need to add 3 new dependencies to
* the project related with the jwt manipulation: jjwt-api, jjwt-impl and jjwt-jackson. These need to be
* added in the pom.
* */
@Service
public class JwtService {

    public String extractUserName(String jwt){
        return null;
    }
}
