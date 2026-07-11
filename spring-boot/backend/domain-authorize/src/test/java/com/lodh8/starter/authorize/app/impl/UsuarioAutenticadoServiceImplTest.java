package com.lodh8.starter.authorize.app.impl;

import com.lodh8.starter.authorize.app.port.OauthUserProviderPort;
import com.lodh8.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;
import com.lodh8.starter.notification.domain.events.NotificacaoTrocaSenhaEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.Status;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoServiceImplTest {

    @Mock
    private OauthUserProviderPort oauthUserProviderPort;

    @Mock
    private UsuarioAutenticadoProviderPort usuarioAutenticadoProviderPort;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private EventPublisherPort events;

    @InjectMocks
    private UsuarioAutenticadoServiceImpl service;

    @Test
    void usuarioAutenticado_shouldDelegateToProviders() {
        var mockOAuthUser = mock(OAuthUser.class);
        var expectedUser = mock(UsuarioAutenticado.class);

        when(oauthUserProviderPort.oauthUser()).thenReturn(mockOAuthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(mockOAuthUser)).thenReturn(expectedUser);

        UsuarioAutenticado result = service.usuarioAutenticado();

        assertEquals(expectedUser, result);
        verify(oauthUserProviderPort, times(1)).oauthUser();
        verify(usuarioAutenticadoProviderPort, times(1)).usuarioAutenticado(mockOAuthUser);
    }

    @Test
    void grupoAutenticado_shouldDelegateToProviders() {
        var mockOAuthUser = mock(OAuthUser.class);
        var expectedGroup = mock(GrupoAutenticado.class);

        when(oauthUserProviderPort.oauthUser()).thenReturn(mockOAuthUser);
        when(usuarioAutenticadoProviderPort.grupo(mockOAuthUser)).thenReturn(expectedGroup);

        GrupoAutenticado result = service.grupoAutenticado();

        assertEquals(expectedGroup, result);
        verify(oauthUserProviderPort, times(1)).oauthUser();
        verify(usuarioAutenticadoProviderPort, times(1)).grupo(mockOAuthUser);
    }

    @Test
    void updateMe_shouldUpdateIdentityAndLocalNome() {
        var oauthUser = mock(OAuthUser.class);
        var usuarioId = UUID.randomUUID();
        var identityId = "identity-id";
        var me = new UsuarioAutenticado(
                usuarioId, identityId, null, "Old", "user@test.com",
                TipoAcesso.USUARIO_SISTEMA, Status.ATIVO, 1
        );
        var updated = new UsuarioAutenticado(
                usuarioId, identityId, null, "New Name", "user@test.com",
                TipoAcesso.USUARIO_SISTEMA, Status.ATIVO, 1
        );

        when(oauthUserProviderPort.oauthUser()).thenReturn(oauthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser))
                .thenReturn(me)
                .thenReturn(updated);

        UsuarioAutenticado result = service.updateMe("New Name");

        assertEquals(updated, result);
        verify(usuarioService).updateNome(identityId, "New Name");
        verify(usuarioAutenticadoProviderPort).atualizarNome(usuarioId, "New Name", oauthUser);
    }

    @Test
    void updateMe_whenNotUsuarioSistema_shouldThrowBadRequest() {
        var oauthUser = mock(OAuthUser.class);
        var me = new UsuarioAutenticado(TipoAcesso.CLIENTE_SISTEMA);

        when(oauthUserProviderPort.oauthUser()).thenReturn(oauthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser)).thenReturn(me);

        assertThrows(BadRequestException.class, () -> service.updateMe("Name"));
        verify(usuarioService, never()).updateNome(any(), any());
    }

    @Test
    void changePassword_shouldChangeAndPublishEvent() {
        var oauthUser = mock(OAuthUser.class);
        var me = new UsuarioAutenticado(
                UUID.randomUUID(), "identity-id", null, "User", "user@test.com",
                TipoAcesso.USUARIO_SISTEMA, Status.ATIVO, 1
        );
        var trocaSenha = new TrocaSenha("old", "newPass", "newPass");

        when(oauthUserProviderPort.oauthUser()).thenReturn(oauthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser)).thenReturn(me);

        service.changePassword(trocaSenha);

        verify(usuarioService).changePassword("identity-id", trocaSenha);
        ArgumentCaptor<NotificacaoTrocaSenhaEvent> captor = ArgumentCaptor.forClass(NotificacaoTrocaSenhaEvent.class);
        verify(events).publishEvent(captor.capture());
        assertEquals("User", captor.getValue().nome());
        assertEquals("user@test.com", captor.getValue().destinatario());
        assertEquals("newPass", captor.getValue().senha());
        assertEquals(TipoNotificacao.EMAIL, captor.getValue().tipoNotificacao());
    }

    @Test
    void changePassword_whenNotUsuarioSistema_shouldThrowBadRequest() {
        var oauthUser = mock(OAuthUser.class);
        var me = new UsuarioAutenticado(TipoAcesso.ANONYMOUS);
        var trocaSenha = new TrocaSenha("old", "newPass", "newPass");

        when(oauthUserProviderPort.oauthUser()).thenReturn(oauthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser)).thenReturn(me);

        assertThrows(BadRequestException.class, () -> service.changePassword(trocaSenha));
        verify(usuarioService, never()).changePassword(any(), any());
        verify(events, never()).publishEvent(any());
    }
}
