package com.localkart.services.store.api.users.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.localkart.services.store.api.common.exceptions.BusinessValidationException;
import com.localkart.services.store.api.users.model.OtpAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class OtpAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = (String) authentication.getPrincipal();
        String otp = (String) authentication.getCredentials();

        boolean isValidUser = userService.validateUser(userName, otp);

        if (isValidUser) {

            SecretKey secretKey = Keys.hmacShaKeyFor("68e30470-d451-4b89-bbd5-9060d361155f".getBytes());
            Instant currInstant = Instant.now();
            Date currentDate = new Date(currInstant.getEpochSecond());
            Date expireAfter = new Date(currInstant.plus(5, ChronoUnit.MINUTES).toEpochMilli());
            String jwt = Jwts.builder()
                    .issuer("store-auth-server")
                    .issuedAt(currentDate)
                    .expiration(expireAfter)
                    .claim("userName", userName)
                    .claim("role", "store_user")
                    .signWith(secretKey)
                    .compact();

            return new OtpAuthentication(userName, jwt, List.of(new SimpleGrantedAuthority("store_user")));
        }
        throw new BusinessValidationException("user not authorized", HttpStatus.FORBIDDEN);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
