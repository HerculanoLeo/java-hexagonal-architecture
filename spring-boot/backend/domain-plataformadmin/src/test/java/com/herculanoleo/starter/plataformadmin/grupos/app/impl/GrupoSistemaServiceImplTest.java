package com.herculanoleo.starter.plataformadmin.grupos.app.impl;

import com.herculanoleo.starter.identity.grupos.app.GrupoService;
import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.plataformadmin.grupos.app.port.GrupoSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoSistemaServiceImplTest {

    @Mock
    private GrupoService grupoService;

    @Mock
    private GrupoSistemaMapperPort mapper;

    @InjectMocks
    private GrupoSistemaServiceImpl service;

    @Test
    void findAll_shouldMapSearchAndMapResult() {
        // Arrange
        var search = mock(GrupoSistemaSearch.class);
        var identitySearch = mock(GrupoSearch.class);
        var identityGrupo = mock(Grupo.class);
        var sistemaGrupo = mock(GrupoSistema.class);

        when(mapper.search(search)).thenReturn(identitySearch);
        when(grupoService.findAll(identitySearch)).thenReturn(List.of(identityGrupo));
        when(mapper.domain(identityGrupo)).thenReturn(sistemaGrupo);

        // Act
        Collection<GrupoSistema> result = service.findAll(search);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(sistemaGrupo));
        verify(mapper).search(search);
        verify(grupoService).findAll(identitySearch);
        verify(mapper).domain(identityGrupo);
    }

    @Test
    void findById_whenFound_shouldMapResult() {
        // Arrange
        String id = "test-id";
        var identityGrupo = mock(Grupo.class);
        var sistemaGrupo = mock(GrupoSistema.class);

        when(grupoService.findById(id)).thenReturn(Optional.of(identityGrupo));
        when(mapper.domain(identityGrupo)).thenReturn(sistemaGrupo);

        // Act
        Optional<GrupoSistema> result = service.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sistemaGrupo, result.get());
        verify(grupoService).findById(id);
        verify(mapper).domain(identityGrupo);
    }

    @Test
    void findById_whenNotFound_shouldReturnEmpty() {
        // Arrange
        String id = "not-found-id";
        when(grupoService.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<GrupoSistema> result = service.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(grupoService).findById(id);
        verify(mapper, never()).domain(any());
    }

    @Test
    void findByIdentityId_whenFound_shouldMapResult() {
        // Arrange
        String identityId = "identity-123";
        var identityGrupo = mock(Grupo.class);
        var sistemaGrupo = mock(GrupoSistema.class);

        when(grupoService.findByIdentityId(identityId)).thenReturn(Optional.of(identityGrupo));
        when(mapper.domain(identityGrupo)).thenReturn(sistemaGrupo);

        // Act
        Optional<GrupoSistema> result = service.findByIdentityId(identityId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sistemaGrupo, result.get());
        verify(grupoService).findByIdentityId(identityId);
        verify(mapper).domain(identityGrupo);
    }

    @Test
    void findByIdentityId_whenNotFound_shouldReturnEmpty() {
        // Arrange
        String identityId = "not-found-identity";
        when(grupoService.findByIdentityId(identityId)).thenReturn(Optional.empty());

        // Act
        Optional<GrupoSistema> result = service.findByIdentityId(identityId);

        // Assert
        assertTrue(result.isEmpty());
        verify(grupoService).findByIdentityId(identityId);
        verify(mapper, never()).domain(any());
    }

    @Test
    void register_shouldMapRequestAndResult() {
        // Arrange
        var register = mock(GrupoSistemaRegister.class);
        var identityRegister = mock(GrupoRegister.class);
        var identityGrupo = mock(Grupo.class);
        var sistemaGrupo = mock(GrupoSistema.class);

        when(mapper.register(register)).thenReturn(identityRegister);
        when(grupoService.register(identityRegister)).thenReturn(identityGrupo);
        when(mapper.domain(identityGrupo)).thenReturn(sistemaGrupo);

        // Act
        GrupoSistema result = service.register(register);

        // Assert
        assertEquals(sistemaGrupo, result);
        verify(mapper).register(register);
        verify(grupoService).register(identityRegister);
        verify(mapper).domain(identityGrupo);
    }

    @Test
    void update_shouldMapRequestAndCallService() {
        // Arrange
        String id = "test-id";
        var update = mock(GrupoSistemaUpdate.class);
        var identityUpdate = mock(GrupoUpdate.class);

        when(mapper.update(update)).thenReturn(identityUpdate);
        doNothing().when(grupoService).update(id, identityUpdate);

        // Act
        service.update(id, update);

        // Assert
        verify(mapper).update(update);
        verify(grupoService).update(id, identityUpdate);
    }

    @Test
    void delete_shouldCallService() {
        // Arrange
        String id = "test-id";
        doNothing().when(grupoService).delete(id);

        // Act
        service.delete(id);

        // Assert
        verify(grupoService).delete(id);
    }
}
