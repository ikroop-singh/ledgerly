package com.ledgerly.app.expense_tracker.filter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserRateLimiterFilter extends OncePerRequestFilter {

    private final RateLimiterRegistry rateLimiterRegistry;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/transactions/add".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {

            String username = getLoggedInUser();

            if (username != null) {
//            RateLimiter rateLimiter = rateLimiterRegistry
//                    .find("api-limit")  // Find the existing instance
//                    .map(existing -> rateLimiterRegistry.rateLimiter(username, existing.getRateLimiterConfig()))  // Create user-specific
//                    .orElseThrow(() -> new IllegalStateException("Rate limiter configuration 'api-limit' not found"));

                //Creating a rate limiter for a particular user
                RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(username, "api-limit");
                if (!rateLimiter.acquirePermission()) {
                    response.setStatus(429);
                    response.getWriter().write("Too many requests for user " + username);
                    return;
                }

            }
        }
        filterChain.doFilter(request,response);
    }

    public String getLoggedInUser(){

        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails)principal).getUsername();
        }
        return null;
    }

}
