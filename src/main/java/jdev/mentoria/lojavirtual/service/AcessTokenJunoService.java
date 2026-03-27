package jdev.mentoria.lojavirtual.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jdev.mentoria.lojavirtual.model.AcessTokenJunoAPI;

/**
 * Serviço responsável por recuperar o token de acesso ativo da API Juno.
 *
 * <p>A API da Juno utiliza tokens de autenticação para permitir que o sistema
 * realize chamadas seguras para os serviços financeiros (pagamentos, boletos,
 * cobranças etc.).</p>
 *
 * <p>Esse serviço consulta o banco de dados e retorna o token que foi
 * previamente armazenado após a autenticação com a API.</p>
 *
 * <p>Em termos simples: ele busca no banco o token atual que o sistema
 * deve usar para conversar com a API da Juno.</p>
 */
@Service
public class AcessTokenJunoService {

    /**
     * EntityManager utilizado para executar consultas JPA diretamente.
     *
     * <p>O EntityManager permite executar JPQL ou SQL sem precisar criar
     * métodos específicos no repositório.</p>
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Busca o token de acesso ativo da API Juno no banco de dados.
     *
     * <p>O método executa uma consulta JPQL na entidade {@link AcessTokenJunoAPI}
     * e retorna apenas o primeiro resultado encontrado.</p>
     *
     * <p>Caso não exista nenhum token armazenado, o método captura
     * a exceção {@link NoResultException} e retorna {@code null}.</p>
     *
     * @return {@link AcessTokenJunoAPI} contendo o token ativo ou {@code null}
     * caso nenhum registro seja encontrado.
     */
    public AcessTokenJunoAPI buscaTokenAtivo() {

        try {

            // ------------------------------------------------------------
            // Executa uma consulta JPQL na entidade AcessTokenJunoAPI
            // ------------------------------------------------------------

            AcessTokenJunoAPI acessTokenJunoAPI =
                    (AcessTokenJunoAPI) entityManager
                            .createQuery("select a from AcessTokenJunoAPI a")

                            // Limita a consulta a apenas um registro
                            .setMaxResults(1)

                            // Retorna o único resultado encontrado
                            .getSingleResult();

            // Retorna o token encontrado
            return acessTokenJunoAPI;

        } catch (NoResultException e) {

            // Caso não exista nenhum token no banco,
            // retorna null para indicar ausência de resultado
            return null;
        }
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Esse serviço existe porque a API da Juno exige um token
     * para autenticar todas as requisições.
     *
     * Normalmente o fluxo funciona assim:
     *
     * 1) O sistema pede um token para a API da Juno
     * 2) A API devolve um token válido por um tempo
     * 3) Esse token é salvo no banco
     * 4) Sempre que o sistema precisar chamar a API,
     *    ele busca o token salvo e usa na requisição
     *
     * Esse método faz exatamente o passo 4.
     *
     * Ele consulta a tabela da entidade AcessTokenJunoAPI
     * e pega apenas um registro (o primeiro).
     *
     * A limitação com setMaxResults(1) existe porque
     * normalmente só precisamos de um token ativo.
     *
     * Se o banco estiver vazio, o JPA lança uma exceção
     * chamada NoResultException.
     *
     * O método captura essa exceção e retorna null,
     * evitando que o sistema quebre.
     *
     * Em resumo:
     * esse método é responsável por recuperar o token
     * que permite que o sistema se autentique na API da Juno.
     * ===============================================================
     */
}