package com.lodh8.starter.security.historico.app.port;

import com.lodh8.starter.security.historico.domain.HistoricoLogin;
import com.lodh8.starter.security.historico.domain.HistoricoLoginRegister;
import com.lodh8.starter.security.historico.domain.HistoricoLoginSearch;

import java.util.Collection;

public interface HistoricoLoginRepositoryPort {

    Collection<HistoricoLogin> findAll(HistoricoLoginSearch search);

    boolean existsBySessaoBffId(String sessaoBffId);

    HistoricoLogin register(HistoricoLoginRegister register);

}
