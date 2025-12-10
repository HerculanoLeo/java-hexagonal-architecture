package com.herculanoleo.starter.plataformadmin.usuarios.infra.persistence;

import com.herculanoleo.starter.plataformadmin.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistema;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaRegister;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaSearch;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaUpdate;
import com.herculanoleo.starter.plataformadmin.usuarios.infra.UsuarioSistemaMapper;
import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.herculanoleo.starter.plataformadmin.usuarios.infra.persistence.UsuarioSistemaEntitySpecification.*;

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
