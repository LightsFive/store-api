package com.localkart.services.store.api.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localkart.services.store.api.common.exceptions.BusinessValidationException;
import com.localkart.services.store.api.common.model.Error;
import com.localkart.services.store.api.users.model.OtpAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String bearerToken = request.getHeader("Authorization");
            String token = bearerToken.contains("Bearer") ? bearerToken.substring(7) : bearerToken;

            SecretKey secretKey = Keys.hmacShaKeyFor("68e30470-d451-4b89-bbd5-9060d361155f".getBytes());
            var jwtParser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

            var claims = jwtParser.parseSignedClaims(token);
            var userName = claims.getPayload().get("userName");
            var role = (String) claims.getPayload().get("role");

            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            var auth = new OtpAuthentication(userName, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (Exception ex) {

            String error = getErrorMessage(new BusinessValidationException(ex.getMessage(), HttpStatus.FORBIDDEN));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(error);
            //throw new BusinessValidationException(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        if (path.equals("/users/login") || path.equals("/products/offers") ||
            path.equals("/users/validate") || path.contains("/products")) {
            return true;
        }
        return false;
    }

    private String getErrorMessage(BusinessValidationException ex) throws JsonProcessingException {
        String errorMessage = ex.getMessage();
        String target = ex.getTarget();
        HttpStatus httpStatus = ex.getHttpStatus();
        Error error = new Error(httpStatus, errorMessage, target);

        return new ObjectMapper().writeValueAsString(error);
    }
}
