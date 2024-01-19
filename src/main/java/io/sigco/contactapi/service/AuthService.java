package io.sigco.contactapi.service;

import  io.sigco.contactapi.constant.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;



import java.util.Date;

@Service
@ComponentScan
public class AuthService {

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Constant.EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, Constant.SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Constant.SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}