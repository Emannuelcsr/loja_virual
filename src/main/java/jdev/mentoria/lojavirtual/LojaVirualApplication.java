package jdev.mentoria.lojavirtual;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Classe principal de inicialização da aplicação Loja Virtual.
 *
 * <p>Essa é a classe que sobe o projeto Spring Boot e também concentra
 * várias configurações globais da aplicação, como:</p>
 *
 * <p>
 * - inicialização do Spring Boot<br>
 * - varredura de entidades JPA<br>
 * - varredura de componentes do Spring<br>
 * - ativação dos repositórios JPA<br>
 * - gerenciamento de transações<br>
 * - execução assíncrona<br>
 * - agendamentos automáticos<br>
 * - configuração do Spring MVC
 * </p>
 *
 * <p>Em termos simples: essa classe é o “centro de partida” da aplicação.
 * É daqui que o sistema começa a rodar e também é aqui que várias peças
 * importantes do Spring são habilitadas.</p>
 */
@SpringBootApplication
@EntityScan(basePackages = "jdev.mentoria.lojavirtual.model")
@ComponentScan(basePackages = { "jdev.*" })
@EnableJpaRepositories(basePackages = { "jdev.mentoria.lojavirtual.repository" })
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableWebMvc
public class LojaVirualApplication implements AsyncConfigurer, WebMvcConfigurer {

    /**
     * Método principal da aplicação.
     *
     * <p>É o ponto de entrada do programa em Java.
     * Quando você executa a aplicação, é este método que o Java chama primeiro.</p>
     *
     * <p>O Spring Boot usa esse método para iniciar o container do Spring,
     * carregar os beans, subir o servidor embutido e deixar a aplicação pronta
     * para receber requisições.</p>
     *
     * @param args argumentos recebidos na inicialização da aplicação.
     */
    public static void main(String[] args) {

        // Inicia a aplicação Spring Boot
        SpringApplication.run(LojaVirualApplication.class, args);
    }

    /**
     * Configura o executor usado pelos métodos assíncronos da aplicação.
     *
     * <p>Esse método é chamado pelo Spring quando você usa {@code @Async}
     * em algum método do sistema. Em vez de executar a tarefa na mesma thread
     * da requisição, o Spring manda a execução para esse pool de threads.</p>
     *
     * <p>Isso é muito útil para tarefas como:</p>
     *
     * <p>
     * - envio de e-mails<br>
     * - processamento em segundo plano<br>
     * - geração de relatórios demorados<br>
     * - integrações externas que não precisam travar a requisição
     * </p>
     *
     * @return {@link Executor} configurado com pool de threads para tarefas assíncronas.
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {

        // Cria o executor que gerencia várias threads
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Quantidade mínima de threads que ficam prontas para uso
        executor.setCorePoolSize(10);

        // Quantidade máxima de threads que podem ser criadas
        executor.setMaxPoolSize(20);

        // Quantidade de tarefas que podem ficar aguardando na fila
        executor.setQueueCapacity(500);

        // Prefixo usado para identificar as threads no log/debug
        executor.setThreadNamePrefix("Assyncrono Thread");

        // Inicializa o executor
        executor.initialize();

        // Retorna o executor pronto para o Spring usar
        return executor;
    }

    /**
     * Configura o resolvedor de views da aplicação MVC.
     *
     * <p>O {@link ViewResolver} é a peça do Spring MVC responsável por descobrir
     * qual arquivo de view deve ser carregado quando um controller retorna
     * um nome de página, como por exemplo:</p>
     *
     * <p>{@code return "cadastro/cadastropessoa";}</p>
     *
     * <p>Com prefixo e sufixo configurados, o Spring monta o caminho completo
     * do arquivo HTML.</p>
     *
     * @return {@link ViewResolver} configurado para localizar as views HTML.
     */
    @Bean
    public ViewResolver viewResolver() {

        // Cria o resolvedor de views do Spring MVC
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        // Define o caminho base onde as views estão localizadas
        viewResolver.setPrefix("classpath:templates/");

        // Define a extensão final das views
        viewResolver.setSuffix(".html");

        
        return viewResolver;
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Essa classe é a classe mais importante do projeto em termos de inicialização.
     * Ela é como a “central elétrica” da aplicação.
     *
     * Vamos separar por partes:
     *
     * 1) @SpringBootApplication
     *    Essa anotação já ativa várias configurações automáticas do Spring Boot.
     *    É ela que transforma essa classe na classe principal da aplicação.
     *
     * 2) @EntityScan
     *    Diz ao Spring onde estão as entidades JPA.
     *    Ou seja: onde estão as classes que representam tabelas do banco.
     *
     * 3) @ComponentScan
     *    Diz ao Spring onde procurar componentes como:
     *    - @Controller
     *    - @Service
     *    - @Repository
     *    - @Component
     *
     * 4) @EnableJpaRepositories
     *    Ativa os repositórios JPA do projeto.
     *    Sem isso, seus repositories poderiam nem ser encontrados.
     *
     * 5) @EnableTransactionManagement
     *    Liga o gerenciamento de transações do Spring.
     *    Isso é o que permite usar @Transactional corretamente.
     *
     * 6) @EnableAsync
     *    Liga a execução assíncrona.
     *    Isso permite usar @Async para rodar tarefas em outra thread.
     *
     * 7) @EnableScheduling
     *    Liga tarefas agendadas com @Scheduled.
     *    Exemplo: tarefas automáticas que rodam de tempos em tempos.
     *
     * 8) @EnableWebMvc
     *    Liga a configuração MVC manual do Spring.
     *    Isso costuma ser usado quando você quer personalizar mais o comportamento web.
     *
     * Sobre o getAsyncExecutor():
     * esse método define o “pool de threads” da aplicação.
     * Quando você usa @Async, é esse executor que vai assumir o trabalho.
     *
     * Sobre o viewResolver():
     * ele serve para transformar nomes de views em caminhos reais de arquivos HTML.
     * Exemplo:
     * se o controller retornar "cadastro/pessoa",
     * o Spring tenta localizar algo como:
     * classpath:templates/cadastro/pessoa.html
     *
     * MAS AQUI TEM UM ERRO IMPORTANTE:
     * no final do método você escreveu:
     *
     *     return viewResolver();
     *
     * Isso está errado.
     *
     * Por quê?
     * Porque o método está chamando ele mesmo de novo.
     * E cada vez que ele chama ele mesmo, entra de novo, entra de novo,
     * entra de novo... até estourar a pilha da aplicação.
     *
     * O correto seria:
     *
     *     return viewResolver;
     *
     * Ou seja: retornar o objeto criado, e não chamar o método outra vez.
     *
     * Em resumo:
     * essa classe sobe o sistema, liga vários módulos do Spring
     * e ainda configura execução assíncrona e resolução de páginas MVC.
     * Mas o método viewResolver, do jeito que está, contém uma recursão infinita
     * e precisa ser corrigido para a aplicação funcionar corretamente.
     * ===============================================================
     */
}