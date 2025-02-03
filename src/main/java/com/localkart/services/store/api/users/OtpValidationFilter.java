package com.localkart.services.store.api.users;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.localkart.services.store.api.users.model.OtpAuthentication;
import com.localkart.services.store.api.users.service.OtpAuthProvider;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretKeyAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class OtpValidationFilter extends OncePerRequestFilter {

    @Autowired
    private OtpAuthProvider otpAuthProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var path = request.getRequestURI();

        if ("/users/validate".equals(path)) {
            String otp = request.getHeader("otp");
            String phoneNumber = request.getHeader("phoneNumber");
            Authentication authentication = otpAuthProvider.authenticate(new OtpAuthentication(phoneNumber, otp));
            response.setHeader("token", (String) authentication.getCredentials());
            response.setStatus(HttpStatus.OK.value());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        if (path.equals("/users/login")) {
            return true;
        }
        return false;
    }
}
