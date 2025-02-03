package com.localkart.services.store.api.users;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class QueryParameterRequestMatcher implements RequestMatcher {

    private String path;

    public QueryParameterRequestMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String servletPAth = request.getServletPath();

        return path.contains(requestPath);
    }
}
