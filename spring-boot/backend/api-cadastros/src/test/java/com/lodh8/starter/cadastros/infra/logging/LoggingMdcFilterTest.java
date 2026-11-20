package com.lodh8.starter.cadastros.infra.logging;

import com.lodh8.starter.shared.utils.LogMdc;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class LoggingMdcFilterTest {

    private final LoggingMdcFilter filter = new LoggingMdcFilter();

    @AfterEach
    void tearDown() {
        LogMdc.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsUidDuringChainAndClearsAfter() throws Exception {
        AtomicReference<String> uidDuring = new AtomicReference<>();
        FilterChain chain = (req, res) -> uidDuring.set(LogMdc.uid());

        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), chain);

        assertNotNull(uidDuring.get());
        assertNull(LogMdc.uid());
        assertNull(LogMdc.usuario());
    }

    @Test
    void setsUsuarioFromPreferredUsername() throws Exception {
        var jwt = new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "none"),
                Map.of("sub", "subject-id", "preferred_username", "admin@dummy.com")
        );
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, List.of()));

        AtomicReference<String> usuarioDuring = new AtomicReference<>();
        FilterChain chain = (req, res) -> usuarioDuring.set(LogMdc.usuario());

        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), chain);

        assertEquals("admin@dummy.com", usuarioDuring.get());
        assertNull(LogMdc.usuario());
    }
}
