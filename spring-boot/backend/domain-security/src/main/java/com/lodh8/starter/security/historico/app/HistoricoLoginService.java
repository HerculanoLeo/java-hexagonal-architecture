package com.lodh8.starter.security.historico.app;

import com.lodh8.starter.security.historico.domain.HistoricoLogin;
import com.lodh8.starter.security.historico.domain.HistoricoLoginSearch;

import java.util.Collection;

public interface HistoricoLoginService {

    Collection<HistoricoLogin> findAll(HistoricoLoginSearch search);

    void register(String ip, String userAgent, String sessaoBffId, String email, String nome);

}
