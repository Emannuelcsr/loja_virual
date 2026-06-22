package jdev.mentoria.lojavirtual.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import jdev.mentoria.lojavirtual.model.ItemVendaLoja;
import jdev.mentoria.lojavirtual.model.NotaFiscalVenda;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.ItemNotaFiscalDTO;
import jdev.mentoria.lojavirtual.model.dto.NotaFiscalDTO;
import jdev.mentoria.lojavirtual.model.dto.NotaFiscalRetornoEnvioDTO;
import jdev.mentoria.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;

@Service
public class FocusNotaFiscalService {

	@Autowired
	private Vd_cp_Loja_virtual_Repository vendaRepository;

	@Autowired
	private NotaFiscalVendaRepository fiscalVendaRepository;

	public String emitirNotaFiscalPorVenda(Long idVenda) throws Exception {

		VendaCompraLojaVirtual venda = vendaRepository.findById(idVenda)
				.orElseThrow(() -> new RuntimeException("Venda não encontrada"));

		String ref = String.valueOf(venda.getId());

		NotaFiscalDTO dto = montarDTO(venda);

		return emitirNotaFiscal(dto, ref);
	}

	/**
	 * Método responsável por converter uma venda do sistema
	 * (VendaCompraLojaVirtual) em um DTO de Nota Fiscal (NotaFiscalDTO), no formato
	 * exigido pela API da Focus NFe.
	 *
	 * <p>
	 * Aqui acontece a transformação dos dados do sistema interno (cliente,
	 * produtos, valores) para o padrão da NFe.
	 * </p>
	 *
	 * <p>
	 * IMPORTANTE: Campos fiscais como CFOP, NCM, ICMS, PIS e COFINS devem ser
	 * definidos com orientação do contador.
	 * </p>
	 *
	 * @param venda objeto da venda contendo cliente, produtos e valores
	 * @return DTO pronto para envio à API da Focus
	 */
	private NotaFiscalDTO montarDTO(VendaCompraLojaVirtual venda) {

		/**
		 * Criação do objeto principal da nota fiscal
		 */
		NotaFiscalDTO dto = new NotaFiscalDTO();

		/**
		 * =============================== DADOS GERAIS DA NOTA
		 * ===============================
		 */

		/** Natureza da operação (ex: Venda) */
		dto.setNaturezaOperacao("Venda");

		/** Data de emissão */
		dto.setDataEmissao(LocalDateTime.now().toString());

		/** Tipo de documento (1 = NFe) */
		dto.setTipoDocumento(1);

		/** Finalidade (1 = emissão normal) */
		dto.setFinalidadeEmissao(1);

		/**
		 * =============================== DADOS DO EMITENTE (EMPRESA)
		 * ===============================
		 */

		dto.setCnpjEmitente("SEU_CNPJ");
		dto.setNomeEmitente("SUA_EMPRESA");
		dto.setNomeFantasiaEmitente("NOME_FANTASIA");

		dto.setUfEmitente("SC");
		dto.setMunicipioEmitente("BALNEARIO CAMBORIU");

		/**
		 * =============================== DADOS DO DESTINATÁRIO (CLIENTE)
		 * ===============================
		 */

		dto.setNomeDestinatario(venda.getPessoa().getNome());
		dto.setCpfDestinatario(venda.getPessoa().getCpf());

		dto.setMunicipioDestinatario(venda.getPessoa().getEnderecos().get(0).getCidade());

		dto.setUfDestinatario(venda.getPessoa().getEnderecos().get(0).getUf());

		/**
		 * =============================== VALORES DA NOTA
		 * ===============================
		 */

		dto.setValorTotal(venda.getValorTotal());
		dto.setValorProdutos(venda.getValorTotal());

		/**
		 * =============================== ITENS DA NOTA (PRODUTOS)
		 * ===============================
		 */

		List<ItemNotaFiscalDTO> items = new ArrayList<>();

		int numeroItem = 1;

		for (ItemVendaLoja itemVenda : venda.getItemVendaLojas()) {

			ItemNotaFiscalDTO item = new ItemNotaFiscalDTO();

			/** Número sequencial do item */
			item.setNumeroItem(numeroItem++);

			/** Descrição do produto */
			item.setDescricao(itemVenda.getProduto().getNome());

			/** Quantidade vendida */
			item.setQuantidadeComercial(BigDecimal.valueOf(itemVenda.getQuantidade()));

			/** Valor unitário */
			item.setValorUnitarioComercial(itemVenda.getProduto().getValorVenda());

			/** Valor total do item */
			item.setValorBruto(itemVenda.getVendaCompraLojaVirtual().getValorTotal());

			/**
			 * =============================== CAMPOS FISCAIS (CONTADOR)
			 * ===============================
			 */

			/** CFOP (ex: 5102 - venda dentro do estado) */
			item.setCfop(5102);

			/** Código NCM do produto */
			item.setCodigoNcm("61091000");

			/** ICMS */
			item.setIcmsSituacaoTributaria("102");
			item.setIcmsOrigem("0");

			/** PIS */
			item.setPisSituacaoTributaria("07");

			/** COFINS */
			item.setCofinsSituacaoTributaria("07");

			items.add(item);
		}

		/**
		 * Associa a lista de itens à nota
		 */
		dto.setItems(items);

		return dto;
	}

	/**
	 * Envia uma NFe para autorização na API Focus NFe.
	 *
	 * Conforme a documentação da Focus:
	 *
	 * POST: https://homologacao.focusnfe.com.br/v2/nfe?ref=REFERENCIA
	 *
	 * A nota é enviada no corpo da requisição em JSON. A API primeiro valida os
	 * dados e depois coloca a nota em uma fila de processamento assíncrono.
	 *
	 * @param notaFiscalDTO DTO contendo os dados da NFe.
	 * @param ref           referência única da nota no seu sistema, exemplo: ID da
	 *                      venda.
	 * @return resposta da Focus em formato JSON.
	 * @throws Exception caso ocorra erro ao converter ou enviar a requisição.
	 */
	public String emitirNotaFiscal(NotaFiscalDTO notaFiscalDTO, String ref) throws Exception {

		/*
		 * Token obtido no cadastro da empresa na Focus. Na Focus, o token é usado como
		 * usuário no Basic Auth. A senha fica vazia.
		 */
		String token = "SEU_TOKEN_DA_FOCUS";

		/*
		 * URL de homologação. Para produção, trocar para:
		 * https://api.focusnfe.com.br/v2/nfe?ref=
		 */
		String url = "https://homologacao.focusnfe.com.br/v2/nfe?ref=" + ref;

		/*
		 * Client HTTP. Para estudo/local, você pode usar seu HostIgnoreClient. Em
		 * produção, o ideal é usar Client.create() normal.
		 */
		Client client = new HostIgnoreClient("homologacao.focusnfe.com.br").hostIgnoreClient();

		/*
		 * Autenticação Basic Auth da Focus: login = token senha = vazia
		 */
		client.addFilter(new HTTPBasicAuthFilter(token, ""));

		/*
		 * Endpoint da API Focus.
		 */
		WebResource webResource = client.resource(url);

		/*
		 * Converte o DTO Java para JSON. Esse JSON será o corpo do POST.
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(notaFiscalDTO);

		/*
		 * Envia a requisição POST para a Focus.
		 */
		ClientResponse response = webResource.accept("application/json;charset=UTF-8")
				.type("application/json;charset=UTF-8").post(ClientResponse.class, json);

		/*
		 * Retorna o corpo da resposta da API. Importante: isso não significa que a nota
		 * já foi autorizada. A Focus processa a nota de forma assíncrona.
		 */
		return response.getEntity(String.class);
	}

	private String enviarParaFocus(NotaFiscalDTO dto, String ref) throws Exception {

		String token = "SEU_TOKEN";
		String url = "https://homologacao.focusnfe.com.br/v2/nfe?ref=" + ref;

		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(token, ""));

		WebResource webResource = client.resource(url);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dto);

		ClientResponse response = webResource.accept("application/json").type("application/json")
				.post(ClientResponse.class, json);

		return response.getEntity(String.class);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/*
	 *
	 * Cancela uma Nota Fiscal já autorizada na Focus NFe.
	 *
	 * <p> Conforme a documentação da Focus: </p>
	 *
	 * DELETE: https://homologacao.focusnfe.com.br/v2/nfe/{ref}
	 *
	 * <p> O cancelamento é síncrono, ou seja: a API já retorna se cancelou ou deu
	 * erro. </p>
	 *
	 * @param ref identificação da nota no seu sistema (ex: id da venda)
	 * 
	 * @param justificativa motivo do cancelamento (mínimo 15 caracteres)
	 * 
	 * @return resposta da API (JSON como String)
	 * 
	 * @throws Exception erro na requisição
	 */
	/**
	 * Cancela uma Nota Fiscal já autorizada na Focus NFe.
	 *
	 * DELETE: https://homologacao.focusnfe.com.br/v2/nfe/{ref}
	 *
	 * @param ref           referência única da nota no seu sistema
	 * @param justificativa motivo do cancelamento, entre 15 e 255 caracteres
	 * @return resposta da API Focus em JSON
	 * @throws Exception caso ocorra erro ao montar JSON ou enviar a requisição
	 */
	public String cancelarNotaFiscal(String ref, String justificativa) throws Exception {

		String token = "SEU_TOKEN_DA_FOCUS";

		String url = "https://homologacao.focusnfe.com.br/v2/nfe/" + ref;

		/**
		 * Cria o corpo JSON do cancelamento. A API espera: { "justificativa": "motivo
		 * do cancelamento" }
		 */
		ObjectMapper mapper = new ObjectMapper();

		Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put("justificativa", justificativa);

		String json = mapper.writeValueAsString(bodyMap);

		/**
		 * Cliente HTTP.
		 */
		Client client = new HostIgnoreClient("homologacao.focusnfe.com.br").hostIgnoreClient();

		/**
		 * Basic Auth da Focus: usuário = token senha = vazia
		 */
		client.addFilter(new HTTPBasicAuthFilter(token, ""));

		/**
		 * Endpoint da nota que será cancelada.
		 */
		WebResource webResource = client.resource(url);

		/**
		 * Requisição DELETE para cancelar a NFe.
		 */
		ClientResponse response = webResource.accept("application/json;charset=UTF-8")
				.type("application/json;charset=UTF-8").delete(ClientResponse.class, json);

		return response.getEntity(String.class);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Consulta uma NFe já enviada para a Focus NFe.
	 *
	 * <p>
	 * Segundo a documentação da Focus, após o envio da nota por POST, é necessário
	 * consultar a nota para saber se ela ainda está em processamento, se foi
	 * autorizada, cancelada ou rejeitada.
	 * </p>
	 *
	 * <p>
	 * Endpoint de homologação: GET
	 * https://homologacao.focusnfe.com.br/v2/nfe/{ref}?completa=1
	 * </p>
	 *
	 * @param ref referência única da nota no seu sistema. Normalmente pode ser o ID
	 *            da venda.
	 * @return resposta da API Focus em formato JSON.
	 * @throws Exception caso ocorra erro na comunicação com a API.
	 */
	public String consultarNotaFiscal(String ref) throws Exception {

		String token = "SEU_TOKEN_DA_FOCUS";

		String url = "https://homologacao.focusnfe.com.br/v2/nfe/" + ref + "?completa=1";

		/**
		 * Cliente HTTP.
		 */
		Client client = new HostIgnoreClient("homologacao.focusnfe.com.br").hostIgnoreClient();

		/**
		 * Basic Auth da Focus: usuário = token senha = vazia
		 */
		client.addFilter(new HTTPBasicAuthFilter(token, ""));

		/**
		 * Recurso/endereço da API que será chamado.
		 */
		WebResource webResource = client.resource(url);

		/**
		 * Requisição GET.
		 *
		 * Consulta não envia body. Apenas busca o status atual da nota.
		 */
		ClientResponse response = webResource.accept("application/json;charset=UTF-8").get(ClientResponse.class);

		return response.getEntity(String.class);
	}

	public NotaFiscalVenda gravaNotaParaVenda(NotaFiscalRetornoEnvioDTO retornoDTO,
			VendaCompraLojaVirtual lojaVirtual) {

		
		NotaFiscalVenda fiscalVendaBuscada = fiscalVendaRepository.buscaNotaPorVendaUnica(lojaVirtual.getId());
		
		
		NotaFiscalVenda notaFiscalVenda = new NotaFiscalVenda();
		
		
		if(fiscalVendaBuscada != null && fiscalVendaBuscada.getId() > 0 ) {
			notaFiscalVenda.setId(fiscalVendaBuscada.getId());
			
		}
		

		notaFiscalVenda.setEmpresa(lojaVirtual.getEmpresa());

		// 🔹 Número da nota
		notaFiscalVenda.setNumeroNota(retornoDTO.getNumero());

		// 🔹 Série
		notaFiscalVenda.setSerieNota(retornoDTO.getSerie());

		// 🔹 Tipo (definido por você, não vem no JSON)
		notaFiscalVenda.setTipoNota("VENDA");

		// 🔹 Caminhos
		notaFiscalVenda.setXml(retornoDTO.getCaminho_xml_nota_fiscal());
		notaFiscalVenda.setPdf(retornoDTO.getCaminho_danfe());

		notaFiscalVenda.setVendaCompraLojaVirtual(lojaVirtual);

		return fiscalVendaRepository.saveAndFlush(notaFiscalVenda);

	}

}
