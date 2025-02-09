package ru.isshepelev.auto.security.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.isshepelev.auto.security.service.JwtCore;
import ru.isshepelev.auto.security.service.impl.UserServiceImpl;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String username = jwtCore.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                    if (jwtCore.isTokenValid(jwt, userDetails)) {
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken token =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        securityContext.setAuthentication(token);
                        SecurityContextHolder.setContext(securityContext);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token is expired");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Authentication error: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
