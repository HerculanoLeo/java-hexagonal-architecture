package com.herculanoleo.starter.cadastros.infra.authorize;

import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.authorize.domain.OAuthUserDetails;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import com.herculanoleo.starter.identity.grupos.app.GrupoService;
import com.herculanoleo.starter.plataformadmin.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistema;
import com.herculanoleo.starter.shared.models.enums.Status;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoProviderAdapterTest {

    private UsuarioSistemaRepositoryPort usuarioSistemaRepository;
    private GrupoService grupoService;
    private CacheManager cacheManager;
    private Cache cache;
    private UsuarioAutenticadoProviderAdapter adapter;

    @BeforeEach
    void setUp() {
        usuarioSistemaRepository = mock(UsuarioSistemaRepositoryPort.class);
        grupoService = mock(GrupoService.class);
        cacheManager = mock(CacheManager.class);
        cache = mock(Cache.class);
        adapter = new UsuarioAutenticadoProviderAdapter(usuarioSistemaRepository, grupoService, cacheManager);
    }

    @Test
    void usuarioAutenticado_whenCacheIsNull_shouldBypassCache() {
        // Arrange
        when(cacheManager.getCache(anyString())).thenReturn(null);

        String identityId = "user-identity-id";
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn(identityId);

        UUID usuarioId = UUID.randomUUID();
        UsuarioSistema usuarioSistema = new UsuarioSistema(
                usuarioId,
                identityId,
                "test@example.com",
                "Test User",
                Status.ATIVO,
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        when(usuarioSistemaRepository.findByIdentityId(identityId)).thenReturn(Optional.of(usuarioSistema));

        // Act
        UsuarioAutenticado result = adapter.usuarioAutenticado(oAuthUser);

        // Assert
        assertNotNull(result);
        assertEquals(usuarioId, result.id());
        verify(cache, never()).get(any(), eq(UsuarioAutenticado.class));
        verify(cache, never()).put(any(), any());
    }

    @Test
    void usuarioAutenticado_whenCacheKeyIsBlank_shouldBypassCache() {
        // Arrange
        when(cacheManager.getCache(anyString())).thenReturn(cache);

        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn(""); // Blank cache key

        String identityId = "user-identity-id";
        when(oAuthUser.userId()).thenReturn(identityId); // for getAuthenticatedUser call

        UUID usuarioId = UUID.randomUUID();
        UsuarioSistema usuarioSistema = new UsuarioSistema(
                usuarioId,
                identityId,
                "test@example.com",
                "Test User",
                Status.ATIVO,
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        when(usuarioSistemaRepository.findByIdentityId(identityId)).thenReturn(Optional.of(usuarioSistema));

        UsuarioAutenticadoProviderAdapter spyAdapter = spy(adapter);
        doReturn("").when(spyAdapter).getCacheKey(oAuthUser);


        // Act
        UsuarioAutenticado result = spyAdapter.usuarioAutenticado(oAuthUser);

        // Assert
        assertNotNull(result);
        assertEquals(usuarioId, result.id());
        verify(cache, never()).get(any(), eq(UsuarioAutenticado.class));
        verify(cache, never()).put(any(), any());
    }

    @Test
    void usuarioAutenticado_whenInCache_shouldReturnCachedUser() {
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn("cached-user");
        UsuarioAutenticado cachedUser = new UsuarioAutenticado(TipoAcesso.USUARIO_SISTEMA);
        when(cache.get("cached-user", UsuarioAutenticado.class)).thenReturn(cachedUser);

        UsuarioAutenticado result = adapter.usuarioAutenticado(oAuthUser);

        assertEquals(cachedUser, result);
        verify(usuarioSistemaRepository, never()).findByIdentityId(any());
    }

    @Test
    void usuarioAutenticado_whenNotInCache_shouldFetchAndCacheUser() {
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        String identityId = "user-identity-id";
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn(identityId);

        UUID usuarioId = UUID.randomUUID();
        UsuarioSistema usuarioSistema = new UsuarioSistema(
                usuarioId,
                identityId,
                "test@example.com",
                "Test User",
                Status.ATIVO,
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        when(usuarioSistemaRepository.findByIdentityId(identityId)).thenReturn(Optional.of(usuarioSistema));
        when(cache.get(identityId, UsuarioAutenticado.class)).thenReturn(null);

        UsuarioAutenticado result = adapter.usuarioAutenticado(oAuthUser);

        assertNotNull(result);
        assertEquals(usuarioId, result.id());
        verify(cache).put(identityId, result);
    }

    @Test
    void getAuthenticatedUser_whenTipoIsUsuarioSistema_andUserExists_shouldReturnUsuarioAutenticado() {
        String identityId = "user-identity-id";
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn(identityId);

        UUID usuarioId = UUID.randomUUID();
        UsuarioSistema usuarioSistema = new UsuarioSistema(
                usuarioId,
                identityId,
                "test@example.com",
                "Test User",
                Status.ATIVO,
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        when(usuarioSistemaRepository.findByIdentityId(identityId)).thenReturn(Optional.of(usuarioSistema));

        UsuarioAutenticado result = adapter.getUsuarioAutenticado(oAuthUser);

        assertNotNull(result);
        assertEquals(usuarioId, result.id());
        assertEquals(identityId, result.identityId());
        assertEquals("Test User", result.nome());
        assertEquals("test@example.com", result.email());
    }

    @Test
    void getUsuarioAutenticadoDoesNotExist_shouldThrowException() {
        String identityId = "non-existent-user";
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.USUARIO_SISTEMA);
        when(oAuthUser.userId()).thenReturn(identityId);

        when(usuarioSistemaRepository.findByIdentityId(identityId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.getUsuarioAutenticado(oAuthUser));
    }

    @Test
    void getUsuarioAutenticado_whenTipoIsClienteSistema_shouldReturnUsuarioAutenticado() {
        String userId = "client-user-id";
        String relacionadoId = UUID.randomUUID().toString();
        OAuthUser oAuthUser = mock(OAuthUser.class);
        OAuthUserDetails details = mock(OAuthUserDetails.class);

        when(oAuthUser.type()).thenReturn(TipoAcesso.CLIENTE_SISTEMA);
        when(oAuthUser.userId()).thenReturn(userId);
        when(oAuthUser.details()).thenReturn(details);
        when(details.relacionadoId()).thenReturn(relacionadoId);

        UsuarioAutenticado result = adapter.getUsuarioAutenticado(oAuthUser);

        assertNotNull(result);
        assertEquals(userId, result.identityId());
        assertEquals(relacionadoId, result.relacionadoId());
    }

    @Test
    void getUsuarioAutenticado() {
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.type()).thenReturn(TipoAcesso.ANONYMOUS);

        UsuarioAutenticado result = adapter.getUsuarioAutenticado(oAuthUser);

        assertEquals(TipoAcesso.ANONYMOUS, result.tipo());
    }

    @Test
    void getCacheKey_withUserId_shouldReturnUserId() {
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn("user-id");

        String key = adapter.getCacheKey(oAuthUser);

        assertEquals("user-id", key);
    }

    @Test
    void getCacheKey_withoutUserId_shouldReturnClientId() {
        OAuthUser oAuthUser = mock(OAuthUser.class);
        when(oAuthUser.userId()).thenReturn(null);
        when(oAuthUser.clientId()).thenReturn("client-id");

        String key = adapter.getCacheKey(oAuthUser);

        assertEquals("client-id", key);
    }
}
