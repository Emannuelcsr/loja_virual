package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;

/**
 * Controller responsável por expor endpoints relacionados
 * ao cadastro de Pessoa Jurídica (empresa).
 *
 * <p>Essa classe recebe requisições HTTP, valida regras básicas
 * e delega a lógica principal para a camada de serviço.</p>
 *
 * <p>Em termos simples: ela é a porta de entrada para cadastrar
 * empresas na loja virtual.</p>
 */
@RestController
public class PessoaController {

    /**
     * Repositório responsável por consultas diretas no banco
     * relacionadas à entidade PessoaJuridica.
     */
    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Serviço que contém a regra de negócio para salvar
     * Pessoa Jurídica e criar usuário automaticamente.
     */
    @Autowired
    private PessoaUserService pessoaUserService;

    /**
     * Endpoint responsável por salvar uma Pessoa Jurídica.
     *
     * <p>Fluxo executado:</p>
     * <p>
     * - valida se o objeto não é nulo<br>
     * - valida se já existe CNPJ cadastrado (em caso de novo registro)<br>
     * - delega a lógica de salvamento para o service<br>
     * - retorna a empresa salva
     * </p>
     *
     * @param pessoaJuridica Objeto recebido no corpo da requisição (JSON).
     * @return ResponseEntity contendo a Pessoa Jurídica salva.
     *
     * @throws ExcepetionLojaVirtual caso alguma regra de negócio seja violada.
     */
    @PostMapping(value = "/salvarpj")
    public ResponseEntity<PessoaJuridica> salvarPJ(
            @RequestBody PessoaJuridica pessoaJuridica)
            throws ExcepetionLojaVirtual {

        // ------------------------------------------------------------
        // 1) Validação básica: objeto não pode ser nulo
        // ------------------------------------------------------------
        if (pessoaJuridica == null) {
            throw new ExcepetionLojaVirtual("Pessoa juridica não pode ser NULL");
        }

        // ------------------------------------------------------------
        // 2) Validação de CNPJ duplicado (apenas para novo cadastro)
        // ------------------------------------------------------------
        if (pessoaJuridica.getId() == null &&
                pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {

            throw new ExcepetionLojaVirtual(
                    "Ja existe CNPJ cadastrado com o numero: " +
                            pessoaJuridica.getCnpj());
        }

        // ------------------------------------------------------------
        // 3) Delegação da regra principal para a camada de serviço
        // ------------------------------------------------------------
        pessoaJuridica =
                pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

        // ------------------------------------------------------------
        // 4) Retorno da resposta HTTP com status 200 (OK)
        // ------------------------------------------------------------
        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Esse controller faz apenas o papel de “porta de entrada”.
     * Ele não concentra regra complexa, apenas validações simples.
     *
     * Primeiro ele verifica se o objeto recebido é nulo.
     * Isso evita NullPointerException e mantém o erro controlado.
     *
     * Depois ele valida uma regra de negócio importante:
     * não pode existir duas empresas com o mesmo CNPJ.
     *
     * Repare que essa validação só acontece quando o ID é null.
     * Isso significa que estamos tratando apenas cadastro novo,
     * e não atualização.
     *
     * Se tudo estiver válido, ele chama o service,
     * que contém a regra pesada:
     * - salvar empresa
     * - ajustar endereços
     * - criar usuário
     * - gerar senha
     * - enviar e-mail
     *
     * Por fim, ele devolve HTTP 200 com o objeto salvo.
     *
     * Se alguma regra for violada, é lançada uma
     * ExcepetionLojaVirtual, que será tratada
     * pelo ControleExcecoes e transformada em
     * uma resposta HTTP adequada (como 404 ou outra).
     *
     * Em resumo:
     * o controller valida, delega e responde.
     * A regra real está no service.
     * ===============================================================
     */
}
