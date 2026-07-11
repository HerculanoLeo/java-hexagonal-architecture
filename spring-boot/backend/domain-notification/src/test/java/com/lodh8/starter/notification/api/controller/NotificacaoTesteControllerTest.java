package com.lodh8.starter.notification.api.controller;

import com.lodh8.starter.notification.app.NotificacaoTesteService;
import com.lodh8.starter.notification.domain.NotificacaoTesteTipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificacaoTesteControllerTest {

    @Mock
    private NotificacaoTesteService service;

    @InjectMocks
    private NotificacaoTesteController controller;

    @Test
    void testarEmail_shouldDelegateToService() {
        ResponseEntity<Void> response = controller.testarEmail(NotificacaoTesteTipo.BOAS_VINDAS);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service).enviarEmailTeste(NotificacaoTesteTipo.BOAS_VINDAS);
    }
}
