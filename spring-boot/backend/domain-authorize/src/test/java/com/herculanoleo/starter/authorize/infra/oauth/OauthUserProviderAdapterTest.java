package com.herculanoleo.starter.authorize.infra.oauth;

import com.herculanoleo.starter.authorize.domain.OAuthUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OauthUserProviderAdapterTest {

    @Mock
    private OAuthUserRequestContextProviderAdapter requestProvider;

    @Mock
    private OAuthUserSingletonProviderAdapter singletonProvider;

    @InjectMocks
    private OauthUserProviderAdapter adapter;

    @Mock
    private RequestAttributes requestAttributes;

    @BeforeEach
    void setUp() {
        // It's good practice to clear the context before each test
        RequestContextHolder.resetRequestAttributes();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void oauthUser_whenRequestContextExists_shouldUseRequestProvider() {
        // Arrange
        RequestContextHolder.setRequestAttributes(requestAttributes);
        OAuthUser expectedUser = mock(OAuthUser.class);
        when(requestProvider.oauthUser()).thenReturn(expectedUser);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertSame(expectedUser, result);
        verify(requestProvider, times(1)).oauthUser();
        verify(singletonProvider, never()).oauthUser();
    }

    @Test
    void oauthUser_whenRequestContextDoesNotExist_shouldUseSingletonProvider() {
        // Arrange
        // No RequestContextHolder is set, so it will be null
        OAuthUser expectedUser = mock(OAuthUser.class);
        when(singletonProvider.oauthUser()).thenReturn(expectedUser);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertSame(expectedUser, result);
        verify(singletonProvider, times(1)).oauthUser();
        verify(requestProvider, never()).oauthUser();
    }
}
