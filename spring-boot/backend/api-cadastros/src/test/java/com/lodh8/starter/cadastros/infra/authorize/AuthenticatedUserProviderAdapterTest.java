package com.lodh8.starter.cadastros.infra.authorize;

import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.OAuthUserDetails;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.backoffice.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.shared.models.enums.Status;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserProviderAdapterTest {

    @Mock
    private UsuarioSistemaRepositoryPort usuarioSistemaRepository;

    @Mock
    private GrupoService grupoService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    @Spy
    private UsuarioAutenticadoProviderAdapter adapter;

    @BeforeEach
    void setUp() {
        // Common setup to simulate cache presence
        lenient().when(cacheManager.getCache(UsuarioAutenticadoProviderAdapter.USER_CACHE_KEY)).thenReturn(cache);
    }

    // Tests for authenticatedUser and its caching
    @Test
    void authenticatedUser_whenInCache_shouldReturnCachedUser() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn("cached-user");
        var cachedUser = mock(UsuarioAutenticado.class);
        when(cache.get("cached-user", UsuarioAutenticado.class)).thenReturn(cachedUser);

        var result = adapter.usuarioAutenticado(oAuthUser);

        assertSame(cachedUser, result);
        verify(adapter, never()).getUsuarioAutenticado(any());
    }

    @Test
    void authenticatedUser_whenNotInCache_shouldFetchAndCacheUser() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn("new-user");
        var fetchedUser = mock(UsuarioAutenticado.class);

        when(cache.get("new-user", UsuarioAutenticado.class)).thenReturn(null);
        doReturn(fetchedUser).when(adapter).getUsuarioAutenticado(oAuthUser);

        var result = adapter.usuarioAutenticado(oAuthUser);

        assertSame(fetchedUser, result);
        verify(adapter).getUsuarioAutenticado(oAuthUser);
        verify(cache).put("new-user", fetchedUser);
    }

    @Test
    void authenticatedUser_whenCacheIsUnavailable_shouldBypassCache() {
        when(cacheManager.getCache(UsuarioAutenticadoProviderAdapter.USER_CACHE_KEY)).thenReturn(null);
        var oAuthUser = mock(OAuthUser.class);
        var fetchedUser = mock(UsuarioAutenticado.class);
        doReturn(fetchedUser).when(adapter).getUsuarioAutenticado(oAuthUser);

        var result = adapter.usuarioAutenticado(oAuthUser);

        assertSame(fetchedUser, result);
        verify(cache, never()).get(any());
        verify(cache, never()).put(any(), any());
    }

    // Tests for getAuthenticatedUser (the core logic)
    @Test
    void getAuthenticatedUser_forUsuarioSistema_shouldReturnUser() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn("user-id");

        var usuarioSistema = mock(UsuarioSistema.class);
        when(usuarioSistema.id()).thenReturn(UUID.randomUUID());
        when(usuarioSistemaRepository.findByIdentityId("user-id")).thenReturn(Optional.of(usuarioSistema));

        var result = adapter.getUsuarioAutenticado(oAuthUser);

        assertNotNull(result);
        assertEquals(usuarioSistema.id(), result.id());
    }

    @Test
    void getAuthenticatedUser_forClienteSistema_shouldReturnUser() {
        var oAuthUser = mock(OAuthUser.class);
        var details = mock(OAuthUserDetails.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.CLIENTE_SISTEMA);
        when(oAuthUser.userId()).thenReturn("client-id");
        when(oAuthUser.details()).thenReturn(details);
        when(details.relacionadoId()).thenReturn("rel-id");

        var result = adapter.getUsuarioAutenticado(oAuthUser);

        assertNotNull(result);
        assertEquals("client-id", result.identityId());
        assertEquals("rel-id", result.relacionadoId());
        assertEquals(TipoAcesso.CLIENTE_SISTEMA, result.tipo());
    }

    @Test
    void getAuthenticatedUser_forDefault_shouldReturnAnonymous() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.ANONYMOUS);

        var result = adapter.getUsuarioAutenticado(oAuthUser);

        assertEquals(TipoAcesso.ANONYMOUS, result.tipo());
    }

    // Tests for the new group method
    @Test
    void group_forUsuarioSistema_whenFound_shouldReturnGroup() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn("user-id");

        var mockGrupo = mock(Grupo.class);
        when(mockGrupo.id()).thenReturn("group-id");
        when(mockGrupo.nome()).thenReturn("Test Group");
        when(mockGrupo.roles()).thenReturn(Collections.singleton("ROLE_TEST"));
        when(grupoService.findByIdentityId("user-id")).thenReturn(Optional.of(mockGrupo));

        GrupoAutenticado result = adapter.grupo(oAuthUser);

        assertNotNull(result);
        assertEquals("group-id", result.id());
        assertEquals("Test Group", result.nome());
        assertTrue(result.roles().contains("ROLE_TEST"));
    }

    @Test
    void group_forUsuarioSistema_whenNotFound_andNotMain_shouldReturnNull() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn("user-id");

        when(grupoService.findByIdentityId("user-id")).thenReturn(Optional.empty());
        when(usuarioSistemaRepository.findByIdentityId("user-id")).thenReturn(Optional.empty());

        assertNull(adapter.grupo(oAuthUser));
    }

    @Test
    void group_forUsuarioSistema_whenNotFound_andMain_shouldReturnSyntheticGroup() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn("user-id");
        when(oAuthUser.roles()).thenReturn(List.of("admin-sistemas"));

        when(grupoService.findByIdentityId("user-id")).thenReturn(Optional.empty());
        when(usuarioSistemaRepository.findByIdentityId("user-id")).thenReturn(Optional.of(
                new UsuarioSistema(
                        UUID.randomUUID(),
                        "user-id",
                        true,
                        "main@example.com",
                        "Main User",
                        Status.ATIVO,
                        1,
                        null,
                        null
                )
        ));

        GrupoAutenticado result = adapter.grupo(oAuthUser);

        assertNotNull(result);
        assertEquals("main", result.id());
        assertEquals("Usuário principal", result.nome());
        assertTrue(result.roles().contains("admin-sistemas"));
    }

    @Test
    void group_forOtherTypes_shouldReturnNull() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.CLIENTE_SISTEMA);

        GrupoAutenticado result = adapter.grupo(oAuthUser);

        assertNull(result);
        verify(grupoService, never()).findByIdentityId(any());
    }

    // Test for getCacheKey
    @Test
    void getCacheKey_withUserId_shouldReturnUserId() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn("user-id");
        assertEquals("user-id", adapter.getCacheKey(oAuthUser));
    }

    @Test
    void getCacheKey_withoutUserId_shouldReturnClientId() {
        var oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn(null);
        when(oAuthUser.clientId()).thenReturn("client-id");
        assertEquals("client-id", adapter.getCacheKey(oAuthUser));
    }
}
