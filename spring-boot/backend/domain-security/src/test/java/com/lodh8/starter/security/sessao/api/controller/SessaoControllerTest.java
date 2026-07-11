package com.lodh8.starter.security.sessao.api.controller;

import com.lodh8.starter.security.sessao.app.SessaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessaoControllerTest {

    @Mock
    private SessaoService service;

    @InjectMocks
    private SessaoController controller;

    @Test
    void invalidateByIdentityId_shouldCallServiceAndReturnAccepted() {
        String identityId = "keycloak-user-id";
        doNothing().when(service).invalidateByIdentityId(identityId);

        ResponseEntity<Void> response = controller.invalidateByIdentityId(identityId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service).invalidateByIdentityId(identityId);
    }

}
