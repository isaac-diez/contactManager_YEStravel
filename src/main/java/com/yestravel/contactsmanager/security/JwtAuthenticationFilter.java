package com.yestravel.contactsmanager.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter (JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getServletPath();
        if (path.startsWith("/auth/") || path.equals("/login") || path.equals("/register")
                || path.startsWith("/css/") || path.startsWith("/js/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getTokenFromHeader(request);
        if (jwt == null) {
            jwt = getTokenFromCookie(request);
            if (jwt != null) {
                logger.info("Token encontrado en la cookie para la ruta: " + request.getServletPath());
            } else {
                logger.warn("NO se encontró token (ni en cabecera ni en cookie) para la ruta: " + request.getServletPath());
            }
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String username = jwtService.extractUsername(jwt);
            logger.info("Intento de autenticación para usuario: " + username); // LOG 1

            // Comprueba que el usuario existe Y que no está ya autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.info("Token VÁLIDO. Autoridades: " + userDetails.getAuthorities()); // LOG 2

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Usuario AUTENTICADO en SecurityContext."); // LOG 3
                } else {
                    logger.warn("Token INVÁLIDO o no coincide con userDetails."); // LOG 4
                }
            }
        } catch (UsernameNotFoundException ex) {
            logger.error("Usuario del token NO ENCONTRADO: " + jwtService.extractUsername(jwt)); // LOG 5
        } catch (Exception e) {
            logger.error("Fallo general de JWT (expirado, firma): " + e.getMessage()); // LOG 6
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
