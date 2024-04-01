package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


/*
* This is the bean that going to manipulate the jwt, to do that, I need to add 3 new dependencies to
* the project related with the jwt manipulation: jjwt-api, jjwt-impl and jjwt-jackson. These need to be
* added in the pom.
* */
@Service
public class JwtService {
    //This key was got it from a page that generate random encryption key. The level security is 256-bit in HEX
    private static final String SECRET_KEY = "E4AEF8E388D69BAF42615AF7222A4CAB9E4DDF4668252547A4334A2F3C";

    public String extractUserName(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    //The next method will help to extract any Claim that I need from the jwt
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }



    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) //To work with jwt, I need to have an internal signature to decrypt or encrypt the token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //The next method helps to generates a new token:
    public String generateToken(
            Map<String, Object> extraClamis, //This is in case I need to add extra claims or extra information into the jwt
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClamis) //Here I set the extra claims
                .setSubject(userDetails.getUsername()) //Here I set the email, which is an important claim
                .setIssuedAt(new Date(System.currentTimeMillis())) //Here I set the today to set the initial data for the token
                .setExpiration(new Date(System.currentTimeMillis() + (100*60*24))) //Here I set the expiration time which is 24 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Here I user the Key I created useing the internal secret_key and I set the encrypted algorithm to use
                .compact();
    }
}
