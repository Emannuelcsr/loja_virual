package jdev.mentoria.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceContagemApi {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ENDPOINT_PF = "END-POINT-NOME-PESSOA-FISICA";

    @Transactional
    public void atualizaAcessoEndPointPF() {

        jdbcTemplate.update(
            "update tabela_acesso_end_point " +
            "set qtd_acesso_end_point = qtd_acesso_end_point + 1 " +
            "where nome_end_point = ?",
            ENDPOINT_PF
        );
    }
}