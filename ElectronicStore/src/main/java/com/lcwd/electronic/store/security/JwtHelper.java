package com.lcwd.electronic.store.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SecretKeyBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//this is used to perform jwt operations

//jwt generate
// username nikalna ho token

@Component
public class JwtHelper {


    //requirement

    //    1. validity
    //validity in millis
    public static final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000;


    //
//    2. secret key
    //mujhe yha secret key application.properties me rkhna hai yaad se bade me jb bhi dkhe phle yhi krna hai
    public static final String SECRET_KEY = "asdogfsaogfnsanfosfhwasfnsofnsfsfosianfowehroiwgafsadfwiotbhwfsfnfwhoahwafnsfawofhwqohtwsnfsaf";


    //getUsernameFromToken hme token se username nikal ke dega
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
//        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

        // 0.12.5
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getPayload();


//        latest
//        SignatureAlgorithm hs512 = SignatureAlgorithm.HS512;
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), hs512.getJcaName());
//        return Jwts.parser().verifyWith(secretKeySpec).build().parseClaimsJws(token).getPayload();
    }


    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //yha getExpirationDateFromToken hme vo date dega jis date pr hmara token expire ho tha
    //upr us date se check kr lenge ki hmara token expire to nhi to rha
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return
                Jwts.builder()
                        .setClaims(claims).
                        setSubject(subject).
                        setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                        .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }


}
