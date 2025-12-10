package com.herculanoleo.starter.plataformadmin.usuarios.infra.persistence;

import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistema;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaRegister;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaSearch;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaUpdate;
import com.herculanoleo.starter.plataformadmin.usuarios.infra.UsuarioSistemaMapper;
import com.herculanoleo.starter.shared.models.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class UsuarioSistemasRepositoryAdapterTest {

    private UsuarioSistemaEntityRepository repository;
    private UsuarioSistemaMapper usuarioSistemaMapper;
    private UsuarioSistemasRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(UsuarioSistemaEntityRepository.class);
        usuarioSistemaMapper = mock(UsuarioSistemaMapper.class);
        adapter = new UsuarioSistemasRepositoryAdapter(repository, usuarioSistemaMapper);
    }

    @Test
    void findAll_shouldReturnMappedDomainObjects() {
        UsuarioSistemaSearch search = new UsuarioSistemaSearch("test", "test@test.com", Status.ATIVO);
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        UsuarioSistema domain = mock(UsuarioSistema.class);

        when(repository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(usuarioSistemaMapper.domain(entity)).thenReturn(domain);

        Collection<UsuarioSistema> result = adapter.findAll(search);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domain, result.iterator().next());
        verify(repository).findAll(any(Specification.class));
        verify(usuarioSistemaMapper).domain(entity);
    }

    @Test
    void findById_whenFound_shouldReturnOptionalOfDomain() {
        UUID id = UUID.randomUUID();
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        UsuarioSistema domain = mock(UsuarioSistema.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(usuarioSistemaMapper.domain(entity)).thenReturn(domain);

        Optional<UsuarioSistema> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(repository).findById(id);
        verify(usuarioSistemaMapper).domain(entity);
    }

    @Test
    void findById_whenNotFound_shouldReturnEmptyOptional() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<UsuarioSistema> result = adapter.findById(id);

        assertTrue(result.isEmpty());
        verify(repository).findById(id);
        verify(usuarioSistemaMapper, never()).domain(any(UsuarioSistemaEntity.class));
    }

    @Test
    void findByEmail_whenFound_shouldReturnOptionalOfDomain() {
        String email = "test@example.com";
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        UsuarioSistema domain = mock(UsuarioSistema.class);

        when(repository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(usuarioSistemaMapper.domain(entity)).thenReturn(domain);

        Optional<UsuarioSistema> result = adapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(repository).findOne(any(Specification.class));
        verify(usuarioSistemaMapper).domain(entity);
    }

    @Test
    void findByIdentityId_whenFound_shouldReturnOptionalOfDomain() {
        String identityId = "identity-id";
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        UsuarioSistema domain = mock(UsuarioSistema.class);

        when(repository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(usuarioSistemaMapper.domain(entity)).thenReturn(domain);

        Optional<UsuarioSistema> result = adapter.findByIdentityId(identityId);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(repository).findOne(any(Specification.class));
        verify(usuarioSistemaMapper).domain(entity);
    }

    @Test
    void findByEmail_whenNotFound_shouldReturnEmptyOptional() {
        String email = "test@example.com";
        when(repository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        Optional<UsuarioSistema> result = adapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(repository).findOne(any(Specification.class));
        verify(usuarioSistemaMapper, never()).domain(any(UsuarioSistemaEntity.class));
    }

    @Test
    void register_shouldSaveAndReturnDomain() {
        UsuarioSistemaRegister registerDTO = mock(UsuarioSistemaRegister.class);
        UsuarioSistemaEntity entityToSave = new UsuarioSistemaEntity();
        UsuarioSistemaEntity savedEntity = new UsuarioSistemaEntity();
        UsuarioSistema domain = mock(UsuarioSistema.class);

        when(usuarioSistemaMapper.entity(registerDTO)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(savedEntity);
        when(usuarioSistemaMapper.domain(savedEntity)).thenReturn(domain);

        UsuarioSistema result = adapter.register(registerDTO);

        assertEquals(domain, result);
        verify(repository).save(entityToSave);
        verify(usuarioSistemaMapper).entity(registerDTO);
        verify(usuarioSistemaMapper).domain(savedEntity);
    }

    @Test
    void update_whenFound_shouldUpdateAndSaveChanges() {
        UUID id = UUID.randomUUID();
        UsuarioSistemaUpdate updateDTO = new UsuarioSistemaUpdate(true,"New Name", "group-id");
        UsuarioSistemaEntity entity = spy(new UsuarioSistemaEntity());

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        adapter.update(id, updateDTO);

        verify(repository).findById(id);
        verify(entity).setNome("New Name");
        verify(repository).save(entity);
    }

    @Test
    void update_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        UsuarioSistemaUpdate updateDTO = new UsuarioSistemaUpdate(true,"New Name", "group-id");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.update(id, updateDTO));
        verify(repository, never()).save(any());
    }

    @Test
    void ativar_whenFound_shouldSetStatusAtivoAndSave() {
        UUID id = UUID.randomUUID();
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        entity.setStatus(Status.INATIVO);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        adapter.ativar(id);

        ArgumentCaptor<UsuarioSistemaEntity> captor = ArgumentCaptor.forClass(UsuarioSistemaEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(Status.ATIVO, captor.getValue().getStatus());
    }

    @Test
    void ativar_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.ativar(id));
        verify(repository, never()).save(any());
    }

    @Test
    void inativar_whenFound_shouldSetStatusInativoAndSave() {
        UUID id = UUID.randomUUID();
        UsuarioSistemaEntity entity = new UsuarioSistemaEntity();
        entity.setStatus(Status.ATIVO);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        adapter.inativar(id);

        ArgumentCaptor<UsuarioSistemaEntity> captor = ArgumentCaptor.forClass(UsuarioSistemaEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(Status.INATIVO, captor.getValue().getStatus());
    }

    @Test
    void inativar_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.inativar(id));
        verify(repository, never()).save(any());
    }
}
