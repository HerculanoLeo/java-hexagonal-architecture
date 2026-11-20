package com.lodh8.starter.cadastros.infra.logging;

import com.lodh8.starter.shared.utils.LogMdc;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Associa cada request a um {@code uid} de log (UUID) e, se autenticado, ao {@code usuario} (sub/azp).
 * Registrado apenas na SecurityFilterChain (após BearerToken), sem {@code @Component}.
 */
public class LoggingMdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        LogMdc.putUid(UUID.randomUUID().toString());
        resolveUsuario().ifPresent(LogMdc::putUsuario);
        try {
            filterChain.doFilter(request, response);
        } finally {
            LogMdc.clear();
        }
    }

    private Optional<String> resolveUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return Optional.empty();
        }
        Jwt jwt = jwtAuth.getToken();
        String preferred = jwt.getClaimAsString("preferred_username");
        if (preferred != null && !preferred.isBlank()) {
            return Optional.of(preferred);
        }
        String azp = jwt.getClaimAsString("azp");
        if (azp != null && !azp.isBlank()) {
            return Optional.of(azp);
        }
        if (jwt.getSubject() != null && !jwt.getSubject().isBlank()) {
            return Optional.of(jwt.getSubject());
        }
        return Optional.empty();
    }
}
