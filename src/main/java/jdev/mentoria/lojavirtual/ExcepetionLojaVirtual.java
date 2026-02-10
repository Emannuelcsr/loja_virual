package jdev.mentoria.lojavirtual;

/**
 * Exceção personalizada da aplicação Loja Virtual.
 *
 * <p>Essa exceção é usada para representar erros de regra de negócio,
 * ou seja, situações esperadas que fazem parte do fluxo normal do sistema,
 * mas que impedem a continuação da operação.</p>
 *
 * <p>Diferente de exceções técnicas (SQL, NullPointer, etc.),
 * essa exceção é lançada quando algo não faz sentido do ponto de vista
 * da lógica do sistema.</p>
 *
 * <p>Exemplos comuns de uso:</p>
 * <p>
 * - tentar buscar um registro que não existe<br>
 * - tentar acessar um recurso inválido<br>
 * - regra de negócio não atendida
 * </p>
 */
public class ExcepetionLojaVirtual extends Exception {

    /**
     * Serial version UID.
     *
     * <p>Usado para controle de versão da classe durante
     * processos de serialização.</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que recebe a mensagem de erro da exceção.
     *
     * <p>A mensagem passada aqui será propagada para quem
     * capturar a exceção e também será usada no retorno
     * da API para o cliente.</p>
     *
     * @param msgErro Mensagem que descreve o erro ocorrido.
     */
    public ExcepetionLojaVirtual(String msgErro) {

        // Chama o construtor da classe Exception,
        // armazenando a mensagem de erro
        super(msgErro);
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Essa classe existe para separar erros de negócio
     * de erros técnicos.
     *
     * Quando você lança uma ExcepetionLojaVirtual, você está dizendo:
     * “algo deu errado na lógica do sistema, mas isso já era esperado
     * e precisa ser comunicado de forma clara”.
     *
     * Em vez de retornar null ou deixar o sistema quebrar
     * com uma exceção genérica, você lança essa exceção
     * com uma mensagem bem definida.
     *
     * Essa exceção é normalmente capturada por um
     * @ExceptionHandler no ControllerAdvice, que converte
     * o erro em uma resposta HTTP adequada, como 404 (Not Found)
     * ou outro status coerente com a regra de negócio.
     *
     * Em resumo:
     * essa classe é a base dos erros controlados do sistema.
     * Ela permite que o backend fale a mesma “língua”
     * que o front-end quando algo não dá certo.
     * ===============================================================
     */
}
