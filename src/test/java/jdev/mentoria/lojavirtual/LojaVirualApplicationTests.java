package jdev.mentoria.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

/**
 * Classe de testes responsável por validar o fluxo completo
 * de cadastro, busca e exclusão da entidade {@link Acesso}.
 *
 * <p>Esse teste sobe o contexto completo do Spring Boot,
 * permitindo testar a aplicação de forma integrada,
 * utilizando banco de dados, repositórios e controllers reais.</p>
 *
 * <p>O objetivo aqui não é testar métodos isolados, mas sim
 * garantir que o fluxo completo do sistema esteja funcionando
 * corretamente do início ao fim.</p>
 */
@SpringBootTest(classes = LojaVirualApplication.class)
class LojaVirualApplicationTests extends TestCase {

    /**
     * Controller responsável por salvar acessos.
     * É injetado pelo Spring durante a execução do teste.
     */
    @Autowired
    private AcessoController acessoController;

    /**
     * Repositório responsável por acessar os dados da tabela de acessos.
     */
    @Autowired
    private AcessoRepository acessoRepository;

    /**
     * Testa o ciclo completo da entidade Acesso:
     *
     * <p>
     * - cadastra um acesso  
     * - valida se foi salvo corretamente  
     * - busca no banco pelo ID  
     * - remove o registro  
     * - valida se a exclusão funcionou  
     * - testa busca customizada por descrição  
     * </p>
     */
    @Test
    void testCadastroAcesso() {

        // ===================== CADASTRO DE UM ACESSO =====================

        // Cria um novo objeto Acesso
        Acesso acesso = new Acesso();

        // Define a descrição da permissão
        acesso.setDescricao("teste");

        // Salva o acesso usando o controller (fluxo real da aplicação)
        acesso = acessoController.salvarAcesso(acesso).getBody();

        // Verifica se o ID foi gerado corretamente (registro persistido)
        assertEquals(true, acesso.getId() > 0);

        // ===================== BUSCA POR ID =====================

        // Busca o mesmo acesso diretamente pelo repositório
        Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();

        // Confirma que o ID retornado é o mesmo que foi salvo
        assertEquals(acesso.getId(), acesso2.getId());

        // ===================== EXCLUSÃO DO REGISTRO =====================

        // Remove o registro do banco
        acessoRepository.deleteById(acesso.getId());

        // Força a execução imediata do DELETE no banco
        acessoRepository.flush();

        // Tenta buscar novamente o registro removido
        Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);

        // Verifica que o registro não existe mais
        assertEquals(true, acesso3 == null);

        // ===================== TESTE DE BUSCA CUSTOMIZADA =====================

        // Cria um novo acesso com padrão de role
        acesso = new Acesso();
        acesso.setDescricao("ROLE_ALUNO");

        // Salva o novo acesso
        acesso = acessoController.salvarAcesso(acesso).getBody();

        // Executa a busca customizada pelo repositório
        List<Acesso> acessos =
                acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());

        // Verifica se encontrou exatamente um registro
        assertEquals(1, acessos.size());

        // Remove o registro criado para não sujar o banco de testes
        acessoRepository.deleteById(acesso.getId());
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Esse teste é um exemplo clássico de teste de integração.
     * Ele não testa apenas um método isolado, mas sim o comportamento
     * real da aplicação rodando com Spring Boot.
     *
     * Primeiro, o teste cria um objeto Acesso e salva usando o
     * AcessoController. Isso garante que a camada de controller,
     * service (se existir) e repositório estão funcionando juntas.
     *
     * Em seguida, o teste busca o mesmo registro diretamente
     * pelo repositório para validar se o dado foi persistido
     * corretamente no banco.
     *
     * Depois disso, o teste remove o registro e força o flush,
     * garantindo que o DELETE seja executado imediatamente.
     * A busca seguinte confirma que o dado realmente não existe mais.
     *
     * Na segunda parte do teste, o foco muda:
     * aqui é validado um método de consulta customizada,
     * que busca acessos pela descrição.
     *
     * Esse tipo de teste é muito importante porque garante que:
     * - o mapeamento JPA está correto
     * - o banco está respondendo como esperado
     * - as regras de negócio funcionam de ponta a ponta
     *
     * Em resumo:
     * esse teste garante que o CRUD de Acesso está íntegro,
     * confiável e pronto para ser usado pelo Spring Security.
     * ===============================================================
     */

}
