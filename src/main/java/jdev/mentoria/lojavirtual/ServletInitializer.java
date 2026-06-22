package jdev.mentoria.lojavirtual;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Classe responsável por inicializar a aplicação Spring Boot
 * quando o projeto é executado como WAR dentro de um servidor externo,
 * como Tomcat.
 *
 * <p>Em outras palavras, essa classe serve como uma "ponte" entre
 * o servidor de aplicação e a sua aplicação Spring Boot.</p>
 *
 * <p>Quando você roda o projeto diretamente pelo método main,
 * normalmente quem inicia tudo é a classe principal da aplicação.
 * Mas quando você faz deploy em um servidor externo, o servidor precisa
 * saber qual classe Spring deve carregar. É exatamente isso que essa
 * classe ajuda a resolver.</p>
 *
 * <p>Ela herda de {@link SpringBootServletInitializer}, que é uma classe
 * do Spring Boot criada justamente para dar suporte a esse tipo de inicialização.</p>
 *
 * <p><b>Exemplo prático:</b><br>
 * Se você empacotar seu projeto como WAR e subir em um Tomcat externo,
 * essa classe será usada para dizer ao Spring: "a aplicação começa
 * por aqui, usando a classe principal LojaVirualApplication".</p>
 *
 * <p><b>Resumo simples:</b><br>
 * Essa classe faz o projeto funcionar corretamente quando ele é publicado
 * em servidor externo, em vez de ser executado apenas pelo main.</p>
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Método chamado pelo Spring Boot durante o processo de inicialização
	 * da aplicação em ambiente de servlet container.
	 *
	 * <p>Esse método informa qual é a classe principal da aplicação
	 * que deve ser usada como ponto de partida para carregar toda
	 * a configuração do Spring Boot.</p>
	 *
	 * <p>Sem isso, o servidor pode não saber de onde começar
	 * a montar o contexto da aplicação.</p>
	 *
	 * @param builder objeto usado para construir e configurar
	 * a aplicação Spring Boot durante a inicialização
	 *
	 * @return o próprio builder configurado com a classe principal
	 * da aplicação
	 *
	 * <p><b>Exemplo simples:</b><br>
	 * Aqui você está dizendo algo como:
	 * "Spring, inicie meu sistema usando a classe LojaVirualApplication".</p>
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

		/*
		 * builder.sources(LojaVirualApplication.class)
		 *
		 * Define qual é a classe principal da aplicação.
		 * Essa classe principal normalmente contém anotações como
		 * @SpringBootApplication e é a base de toda a configuração
		 * do sistema.
		 *
		 * Ou seja:
		 * essa linha fala para o Spring onde a aplicação começa.
		 */
		return builder.sources(LojaVirualApplication.class);
	}

}

/**
 * Resumo didático da classe:
 *
 * Essa classe não contém regra de negócio, não busca dados no banco
 * e não atende requisição HTTP diretamente.
 *
 * O papel dela é apenas ajudar o Spring Boot a iniciar corretamente
 * quando o projeto for implantado em um servidor externo, como um Tomcat.
 *
 * Pense assim:
 *
 * - a classe principal da aplicação é o "coração" do sistema
 * - esta classe aqui aponta para esse coração
 * - o servidor usa essa informação para conseguir ligar o sistema
 *
 * Então, de forma bem simples:
 * essa classe é uma classe de inicialização do projeto em ambiente web.
 */