package com.lodh8.starter.backoffice.usuarios.infra.persistence;

import com.lodh8.starter.backoffice.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaSearch;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaUpdate;
import com.lodh8.starter.backoffice.usuarios.infra.UsuarioSistemaMapper;
import com.lodh8.starter.shared.models.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.lodh8.starter.backoffice.usuarios.infra.persistence.UsuarioSistemaEntitySpecification.*;

@Service
@RequiredArgsConstructor
public class UsuarioSistemasRepositoryAdapter implements UsuarioSistemaRepositoryPort {

    private final UsuarioSistemaEntityRepository repository;

    private final UsuarioSistemaMapper usuarioSistemaMapper;

    @Override
    public Collection<UsuarioSistema> findAll(UsuarioSistemaSearch requestEntity) {
        var entities = this.repository.findAll(spec()
                .and(likeNome(requestEntity.nome()))
                .and(likeEmail(requestEntity.email()))
                .and(status(requestEntity.status())));
        return entities.stream().map(usuarioSistemaMapper::domain).toList();
    }

    public Optional<UsuarioSistema> findById(UUID id) {
        return this.repository.findById(id).map(usuarioSistemaMapper::domain);
    }

    public Optional<UsuarioSistema> findByEmail(String email) {
        return this.repository.findOne(email(email)).map(usuarioSistemaMapper::domain);
    }

    public Optional<UsuarioSistema> findByIdentityId(String identityId) {
        return this.repository.findOne(identityId(identityId)).map(usuarioSistemaMapper::domain);
    }

    @Transactional
    public UsuarioSistema register(UsuarioSistemaRegister requestEntity) {
        var entity = this.repository.save(usuarioSistemaMapper.entity(requestEntity));
        return usuarioSistemaMapper.domain(entity);
    }

    @Transactional
    public void update(UUID id, UsuarioSistemaUpdate requestEntity) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setNome(requestEntity.nome());
        entity.setMain(requestEntity.main());
        this.repository.save(entity);
    }

    @Transactional
    public void updateNome(UUID id, String nome) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setNome(nome);
        this.repository.save(entity);
    }

    @Transactional
    public void ativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setStatus(Status.ATIVO);
        this.repository.save(entity);
    }

    @Transactional
    public void inativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setStatus(Status.INATIVO);
        this.repository.save(entity);
    }

}
