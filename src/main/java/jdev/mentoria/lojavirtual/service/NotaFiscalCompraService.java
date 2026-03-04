package jdev.mentoria.lojavirtual.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdAlertaEstoque;
import jdev.mentoria.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO;

/**
 * Serviço responsável por gerar relatório de produtos comprados em notas fiscais de compra.
 *
 * <p>Essa classe usa {@link JdbcTemplate} para executar uma consulta SQL direta no banco,
 * juntando várias tabelas (nota fiscal, itens, produto e fornecedor) e devolvendo um
 * DTO pronto para ser usado em tela de relatório.</p>
 *
 * <p>O diferencial aqui é que o SQL é montado dinamicamente com base nos filtros preenchidos
 * no DTO de entrada, como data inicial/final, código da nota, código do produto e buscas por nome.</p>
 *
 * <p>Em termos simples: este service é o “gerador de relatório” que busca no banco
 * os produtos comprados, com filtros opcionais, e devolve tudo em forma de lista.</p>
 */
@Service
public class NotaFiscalCompraService {

    /**
     * JdbcTemplate do Spring usado para executar SQL diretamente.
     *
     * <p>Ele cuida de abrir conexão, executar a query e mapear resultados.
     * Aqui usamos ele porque queremos um relatório com joins, colunas específicas
     * e retorno em DTO, sem precisar montar Entity/Repository para isso.</p>
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Gera um relatório de produtos comprados em notas fiscais de compra, aplicando filtros opcionais.
     *
     * <p>O método monta uma query SQL base e vai adicionando condições no WHERE conforme
     * os campos do DTO forem preenchidos. No final, ele executa a query e faz o mapeamento
     * automático do resultado para {@link ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO}.</p>
     *
     * <p>Campos típicos retornados:</p>
     * <p>
     * - código e nome do produto<br>
     * - valor de venda do produto<br>
     * - quantidade comprada na nota<br>
     * - código e nome do fornecedor<br>
     * - data da compra
     * </p>
     *
     * @param dto Objeto com filtros do relatório (datas, códigos e nomes). Campos podem vir vazios.
     * @return List lista de DTOs com os dados do relatório já preenchidos.
     */
    public List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO> gerarRelatorioProdCompraNota(@RequestBody @Valid
            ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO dto) {

        // ------------------------------------------------------------
        // 1) Monta o SQL base do relatório (sem filtros)
        // ------------------------------------------------------------

        // Query base com JOINs entre nota fiscal, itens, produto e fornecedor
        String sql =
                " select " +
                "   p.id as codProduto, " +
                "   p.nome as nomeProduto, " +
                "   p.valor_venda as valorVenda, " +
                "   ntp.quantidade as quantidadeComprada, " +
                "   pj.id as codigoFornecedor, " +
                "   pj.razao_social as nomeFornecedor, " +
                "   cfc.data_compra as dataCompra " +
                " from nota_fiscal_compra cfc " +
                " inner join nota_item_produto ntp on cfc.id = ntp.nota_fiscal_compra_id " +
                " inner join produto p on p.id = ntp.produto_id " +
                " inner join pessoa_juridica pj on pj.id = cfc.pessoa_id " +
                " where 1=1 ";

        // ------------------------------------------------------------
        // 2) Aplica filtros opcionais (só entra se o campo vier preenchido)
        // ------------------------------------------------------------

        // ---- Filtro por data inicial (compra >= dataInicial)
        if (dto.getDataInicial() != null && !dto.getDataInicial().isEmpty()) {
            sql += " and cfc.data_compra >= '" + dto.getDataInicial() + "' ";
        }

        // ---- Filtro por data final (compra <= dataFinal)
        if (dto.getDataFinal() != null && !dto.getDataFinal().isEmpty()) {
            sql += " and cfc.data_compra <= '" + dto.getDataFinal() + "' ";
        }

        // ---- Filtro por código da nota (id da nota)
        if (dto.getCodNota() != null && !dto.getCodNota().isEmpty()) {
            sql += " and cfc.id = " + dto.getCodNota() + " ";
        }

        // ---- Filtro por código do produto (id do produto)
        if (dto.getCodProduto() != null && !dto.getCodProduto().isEmpty()) {
            sql += " and p.id = " + dto.getCodProduto() + " ";
        }

        // ---- Filtro por nome do produto (LIKE)
        if (dto.getNomeProduto() != null && !dto.getNomeProduto().isEmpty()) {
            sql += " and upper(p.nome) like upper('%" + dto.getNomeProduto() + "%') ";
        }

        // ---- Filtro por nome do fornecedor (LIKE)
        if (dto.getNomeFornecedor() != null && !dto.getNomeFornecedor().isEmpty()) {
            sql += " and upper(pj.razao_social) like upper('%" + dto.getNomeFornecedor() + "%') ";
        }

        // ------------------------------------------------------------
        // 3) Executa a query e mapeia o resultado para o DTO automaticamente
        // ------------------------------------------------------------

        return jdbcTemplate.query(
                sql,
                // BeanPropertyRowMapper tenta mapear colunas "as nomeCampo" para setters do DTO
                new BeanPropertyRowMapper<>(ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO.class)
        );

        /*
         * Observação importante:
         * Aqui o SQL está sendo montado por concatenação de strings.
         * Isso funciona, mas em cenários reais é mais seguro usar parâmetros
         * (PreparedStatement) para evitar SQL Injection e problemas com aspas.
         * Vou explicar isso melhor na explicação didática abaixo.
         */
    }

    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Esse service monta um relatório com SQL puro porque relatórios costumam precisar:
     * - juntar várias tabelas (JOIN)
     * - selecionar colunas específicas
     * - devolver em DTO (não em Entity)
     *
     * Ele começa criando um SQL base com os JOINs:
     * - nota_fiscal_compra (cfc) -> é a nota de compra
     * - nota_item_produto (ntp)  -> itens da nota
     * - produto (p)              -> dados do produto
     * - pessoa_juridica (pj)     -> fornecedor da nota
     *
     * Depois ele usa o "where 1=1" para facilitar:
     * como já existe um WHERE fixo, ele pode ir adicionando "and ..." sem precisar
     * ficar checando se já colocou WHERE ou não.
     *
     * A parte dos filtros é opcional:
     * se o DTO vier com dataInicial, ele filtra por data.
     * se vier com codProduto, filtra pelo produto, etc.
     * Se não vier, ele simplesmente não adiciona aquele "and".
     *
     * No final, o JdbcTemplate executa a query e o BeanPropertyRowMapper pega
     * cada linha do resultado e tenta “encaixar” no DTO.
     * Por isso você usou "as codProduto", "as nomeProduto", etc:
     * isso faz o nome da coluna bater com os nomes de atributos/setters do DTO.
     *
     * IMPORTANTE (bem importante):
     * Esse SQL está sendo montado com concatenação de strings.
     * Isso pode abrir brecha para SQL Injection se algum campo vier do usuário.
     * O ideal é montar com parâmetros (?), e passar os valores separadamente.
     * Assim o banco trata tudo como dado, não como comando SQL.
     *
     * Em resumo:
     * esse método é um gerador de relatório: monta SQL + aplica filtros + retorna DTO.
     * ===============================================================
     */
    
    
    
    
    public List<ObjetoRequisicaoRelatorioProdAlertaEstoque> gerarRelatorioAlertaEstoque (@RequestBody @Valid ObjetoRequisicaoRelatorioProdAlertaEstoque alertaEstoque ){
    	
    	
    	String sql =
    	        " select " +
    	        " p.id as codProduto, " +
    	        " p.nome as nomeProduto, " +
    	        " p.valor_venda as valorVenda, " +
    	        " ntp.quantidade as quantidadeComprada, " +
    	        " pj.id as codigoFornecedor, " +
    	        " pj.razao_social as nomeFornecedor, " +
    	        " cfc.data_compra as dataCompra, " +
    	        " p.quantidade_estoque as qtdEstoque, " +
    	        " p.quantidade_alerta_estoque as qtdAlertaEstoque " +
    	        " from nota_fiscal_compra cfc " +
    	        " inner join nota_item_produto ntp on cfc.id = ntp.nota_fiscal_compra_id " +
    	        " inner join produto p on p.id = ntp.produto_id " +
    	        " inner join pessoa_juridica pj on pj.id = cfc.pessoa_id " +
    	        " where 1=1 " +
    	        " and p.quantidade_estoque <= p.quantidade_alerta_estoque " +
    	        " and p.alerta_quantidade_estoque = true ";

     
        if (alertaEstoque.getDataInicial() != null && !alertaEstoque.getDataInicial().isEmpty()) {
            sql += " and cfc.data_compra >= '" + alertaEstoque.getDataInicial() + "' ";
        }

      
        if (alertaEstoque.getDataFinal() != null && !alertaEstoque.getDataFinal().isEmpty()) {
            sql += " and cfc.data_compra <= '" + alertaEstoque.getDataFinal() + "' ";
        }
        
        
    
        if (alertaEstoque.getCodNota() != null && !alertaEstoque.getCodNota().isEmpty()) {
            sql += " and cfc.id = " + alertaEstoque.getCodNota() + " ";
        }

        if (alertaEstoque.getCodProduto() != null && !alertaEstoque.getCodProduto().isEmpty()) {
            sql += " and p.id = " + alertaEstoque.getCodProduto() + " ";
        }

        if (alertaEstoque.getNomeProduto() != null && !alertaEstoque.getNomeProduto().isEmpty()) {
            sql += " and upper(p.nome) like upper('%" + alertaEstoque.getNomeProduto() + "%') ";
        }

        if (alertaEstoque.getNomeFornecedor() != null && !alertaEstoque.getNomeFornecedor().isEmpty()) {
            sql += " and upper(pj.razao_social) like upper('%" + alertaEstoque.getNomeFornecedor() + "%') ";
        }


        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(ObjetoRequisicaoRelatorioProdAlertaEstoque.class));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}