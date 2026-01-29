package jdev.mentoria.lojavirtual.enums;





/**
 * Enum responsável por representar os tipos de endereço utilizados no sistema.
 *
 * <p>Esse enum define valores fixos para classificar endereços,
 * evitando o uso de Strings soltas no código e prevenindo erros
 * de digitação e inconsistência de dados.</p>
 *
 * <p>No sistema, ele é usado para diferenciar endereços de
 * cobrança e endereços de entrega.</p>
 */
public enum TipoEndereco {

	
	
	
	
    /**
     * Endereço utilizado para cobrança.
     */
    COBRANCA("Cobrança"),

    
    
    
    
    /**
     * Endereço utilizado para entrega de produtos.
     */
    ENTREGA("Entrega");

	
	
	
	
	
    /**
     * Descrição legível do tipo de endereço.
     * Esse valor é pensado para exibição ao usuário.
     */
    private String descricao;

    
    
    
    
    
    
    
    
    /**
     * Construtor do enum.
     *
     * <p>O Java chama esse construtor automaticamente
     * ao inicializar cada valor do enum.</p>
     *
     * @param descricao Texto amigável que representa o tipo de endereço.
     */
    private TipoEndereco(String descricao) {
        this.descricao = descricao;
    }

    
    
    
    
    
    
    /**
     * Retorna a descrição legível do tipo de endereço.
     *
     * @return String descrição amigável.
     */
    public String getDescricao() {
        return descricao;
    }

    
    
    
    
    /**
     * Retorna a representação textual do enum.
     *
     * <p>Ao sobrescrever o toString(), garantimos que,
     * sempre que o enum for exibido automaticamente,
     * o texto amigável será mostrado.</p>
     *
     * @return String descrição do tipo de endereço.
     */
    @Override
    public String toString() {
        return this.descricao;
    }

    
    
    
    
    
    
    
    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Esse enum existe para resolver um problema comum em sistemas:
     * representar tipos fixos de forma segura.
     *
     * Em vez de espalhar Strings como "Cobrança" ou "Entrega" pelo código,
     * o sistema passa a usar valores controlados: COBRANCA e ENTREGA.
     * Isso evita erro de digitação, facilita manutenção e deixa o código
     * mais confiável.
     *
     * O nome do enum (COBRANCA / ENTREGA) é o valor técnico usado pelo sistema
     * e normalmente é o que vai para o banco de dados.
     *
     * Já o atributo "descricao" é o valor pensado para o usuário final.
     * Ele é usado em telas, selects, tabelas e relatórios.
     *
     * O método getDescricao() permite acessar esse texto diretamente.
     * O método toString() foi sobrescrito para garantir que,
     * sempre que o enum for exibido automaticamente,
     * o usuário veja "Cobrança" ou "Entrega", e não o nome técnico.
     *
     * Em resumo:
     * este enum separa claramente o que o sistema entende
     * do que o usuário enxerga, mantendo o código limpo,
     * organizado e fácil de manter.
     * ===============================================================
     */

}
