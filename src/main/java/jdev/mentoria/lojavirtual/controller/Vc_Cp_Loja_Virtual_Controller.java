package jdev.mentoria.lojavirtual.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ApiTokenIntegracao;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.enums.StatusContaReceber;
import jdev.mentoria.lojavirtual.model.ContaReceber;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.ItemVendaLoja;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.Produto;
import jdev.mentoria.lojavirtual.model.StatusRastreio;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.ConsutaFreteDTO;
import jdev.mentoria.lojavirtual.model.dto.EmpresaTransporteDTO;
import jdev.mentoria.lojavirtual.model.dto.EnvioEtiquetaDTO;
import jdev.mentoria.lojavirtual.model.dto.ItemVendaDTO;
import jdev.mentoria.lojavirtual.model.dto.ObjetoPostCarneJuno;
import jdev.mentoria.lojavirtual.model.dto.ProductsEnvioEtiquetaDTO;
import jdev.mentoria.lojavirtual.model.dto.ProdutoDTO;
import jdev.mentoria.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojavirtual.model.dto.VolumesEnvioEtiquetaDTO;
import jdev.mentoria.lojavirtual.repository.ContaReceberRepository;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.mentoria.lojavirtual.repository.StatusRastreioRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;
import jdev.mentoria.lojavirtual.service.SendEmailService;
import jdev.mentoria.lojavirtual.service.ServiceJunoBoleto;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class Vc_Cp_Loja_Virtual_Controller {

	private final CupomDescontoController cupomDescontoController;

	@Autowired
	private Vd_cp_Loja_virtual_Repository vd_cp_Loja_virtual_Repository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaController pessoaController;

	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;

	@Autowired
	private ContaReceberRepository contaReceberRepository;

	@Autowired
	private SendEmailService emailService;

	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	Vc_Cp_Loja_Virtual_Controller(CupomDescontoController cupomDescontoController) {
		this.cupomDescontoController = cupomDescontoController;
	}

	@PostMapping(value = "/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual)
			throws ExcepetionLojaVirtual, UnsupportedEncodingException, MessagingException {

		PessoaFisica pessoaFisica = pessoaController.salvarPF(vendaCompraLojaVirtual.getPessoa()).getBody();

		vendaCompraLojaVirtual.setPessoa(pessoaFisica);

		// 2) Endereços: como veio só ID, pega referência do banco (não salva de novo)
		Endereco cobranca = enderecoRepository.getReferenceById(vendaCompraLojaVirtual.getEnderecoCobranca().getId());
		vendaCompraLojaVirtual.setEnderecoCobranca(cobranca);

		Endereco entrega = enderecoRepository.getReferenceById(vendaCompraLojaVirtual.getEnderecoEntrega().getId());
		vendaCompraLojaVirtual.setEnderecoEntrega(entrega);

		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		for (int x = 0; x < vendaCompraLojaVirtual.getItemVendaLojas().size(); x++) {

			vendaCompraLojaVirtual.getItemVendaLojas().get(x).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(x).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		}

		// 3) Salva primeiro a venda
		VendaCompraLojaVirtual vendaSalva = vd_cp_Loja_virtual_Repository.saveAndFlush(vendaCompraLojaVirtual);

	

		// associa a venda grava ao banco com a NF
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// persiste novamente a nota pra ficar amarrada na venda
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		VendaCompraLojaVirtualDTO virtualDTO = new VendaCompraLojaVirtualDTO();

		virtualDTO.setValorTotal(vendaSalva.getValorTotal());
		virtualDTO.setPessoa(vendaSalva.getPessoa());
		virtualDTO.setEntrega(vendaSalva.getEnderecoEntrega());
		virtualDTO.setCobranca(vendaSalva.getEnderecoCobranca());

		virtualDTO.setValorDesconto(vendaSalva.getValorDesconto());

		virtualDTO.setValorFrete(vendaSalva.getValorFrete());

		virtualDTO.setId(vendaSalva.getId());

		virtualDTO.setEmpresa(vendaSalva.getEmpresa().getId());

		for (ItemVendaLoja itemVendaLoja : vendaSalva.getItemVendaLojas()) {

			ItemVendaDTO itemVendaDTOs = new ItemVendaDTO();

			Produto produto = itemVendaLoja.getProduto();

			ProdutoDTO produtoDTO = new ProdutoDTO();
			produtoDTO.setId(produto.getId());
			produtoDTO.setNome(produto.getNome());
			produtoDTO.setValorVenda(produto.getValorVenda());

			itemVendaDTOs.setProduto(produtoDTO);
			itemVendaDTOs.setQuantidade(itemVendaLoja.getQuantidade());

			virtualDTO.getItemVendaDTOs().add(itemVendaDTOs);
		}

		ContaReceber contaReceber = new ContaReceber();

		contaReceber.setDescricao("Venda da loja Virtual No. " + virtualDTO.getId());
		contaReceber.setDatePagamento(Calendar.getInstance().getTime());
		contaReceber.setDateVencimento(Calendar.getInstance().getTime());

		contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		contaReceber.setPessoa(virtualDTO.getPessoa());

		contaReceber.setStatusContaReceber(StatusContaReceber.QUITADA);

		contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());

		contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

		contaReceberRepository.saveAndFlush(contaReceber);

		StringBuilder msgEmail = new StringBuilder();

		msgEmail.append("<h2>Compra realizada com sucesso!</h2>");
		msgEmail.append("<p>Olá, ").append(pessoaFisica.getNome()).append("</p>");
		msgEmail.append("<p>Recebemos sua compra e já estamos processando o pedido.</p>");
		msgEmail.append("<p><strong>Número do pedido:</strong> ").append(vendaCompraLojaVirtual.getId()).append("</p>");
		msgEmail.append("<p>Obrigado por comprar conosco!</p>");

		emailService.enviarEmailHtml("Compra realizada com sucesso", msgEmail.toString(), pessoaFisica.getEmail());

		msgEmail = new StringBuilder();

		msgEmail.append("<h2>Nova venda realizada!</h2>");
		msgEmail.append("<p><strong>Cliente:</strong> ").append(vendaCompraLojaVirtual.getPessoa().getNome())
				.append("</p>");

		msgEmail.append("<p><strong>Número do Pedido:</strong> ").append(vendaCompraLojaVirtual.getId()).append("</p>");

		msgEmail.append("<p><strong>Valor Total:</strong> R$ ").append(vendaCompraLojaVirtual.getValorTotal())
				.append("</p>");

		msgEmail.append("<p><strong>Data da Compra:</strong> ").append(vendaCompraLojaVirtual.getDataVenda())
				.append("</p>");

		msgEmail.append("<hr>");
		msgEmail.append("<p>Acesse o sistema para visualizar os detalhes completos.</p>");

		emailService.enviarEmailHtml("Venda realizada com sucesso", msgEmail.toString(),
				vendaCompraLojaVirtual.getEmpresa().getEmail());

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(virtualDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("id") Long id) {

		VendaCompraLojaVirtual vendaBuscada = vd_cp_Loja_virtual_Repository.findByIdExclusao(id);

		if (vendaBuscada == null) {

			vendaBuscada = new VendaCompraLojaVirtual();
		}

		VendaCompraLojaVirtualDTO virtualDTO = new VendaCompraLojaVirtualDTO();

		virtualDTO.setValorTotal(vendaBuscada.getValorTotal());
		virtualDTO.setPessoa(vendaBuscada.getPessoa());
		virtualDTO.setEntrega(vendaBuscada.getEnderecoEntrega());
		virtualDTO.setCobranca(vendaBuscada.getEnderecoCobranca());

		virtualDTO.setValorDesconto(vendaBuscada.getValorDesconto());

		virtualDTO.setId(vendaBuscada.getId());

		for (ItemVendaLoja itemVendaLoja : vendaBuscada.getItemVendaLojas()) {

			ItemVendaDTO itemVendaDTOs = new ItemVendaDTO();

			Produto produto = itemVendaLoja.getProduto();

			ProdutoDTO produtoDTO = new ProdutoDTO();
			produtoDTO.setId(produto.getId());
			produtoDTO.setNome(produto.getNome());
			produtoDTO.setValorVenda(produto.getValorVenda());

			itemVendaDTOs.setProduto(produtoDTO);
			itemVendaDTOs.setQuantidade(itemVendaLoja.getQuantidade());

			virtualDTO.getItemVendaDTOs().add(itemVendaDTOs);
		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(virtualDTO, HttpStatus.OK);

	}

	@DeleteMapping("/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vd_cp_Loja_virtual_Repository.exclusaoTotalVendaBanco(idVenda);

		return new ResponseEntity<String>("Deletado dados da venda com sucesso", HttpStatus.OK);

	}

	@DeleteMapping("/deleteVendaLogicaBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaLogicaBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vd_cp_Loja_virtual_Repository.exclusaoLogicaVendaBanco(idVenda);

		return new ResponseEntity<String>("Deletado dados da venda com sucesso", HttpStatus.OK);

	}

	@PutMapping("/ativaVendaRegistroBanco/{idVenda}")
	public ResponseEntity<String> ativaVendaRegistroBanco(@PathVariable(value = "idVenda") Long idVenda) {

		vd_cp_Loja_virtual_Repository.ativaVendaBanco(idVenda);

		return new ResponseEntity<String>("Venda recuperada com sucesso", HttpStatus.OK);

	}

	/**
	 * Consulta todas as vendas que contenham um determinado produto.
	 *
	 * <p>
	 * Este endpoint recebe o ID de um produto e retorna uma lista de vendas
	 * (convertidas para DTO) que possuem esse produto em seus itens. Apenas vendas
	 * não excluídas (exclusão lógica) são consideradas.
	 * </p>
	 *
	 * @param idProduto ID do produto utilizado para filtrar as vendas
	 * @return ResponseEntity contendo lista de VendaCompraLojaVirtualDTO com status
	 *         HTTP 200 (OK)
	 */
	@GetMapping(value = "/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(
			@PathVariable("id") Long idProduto) {

		// Busca no repository todas as vendas que possuem o produto informado
		List<VendaCompraLojaVirtual> vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorProduto(idProduto);

		// Garante que a lista não seja null (Spring normalmente retorna lista vazia)
		if (vendaBuscada == null) {
			vendaBuscada = new ArrayList<VendaCompraLojaVirtual>();
		}

		// Lista que será retornada ao cliente (DTO)
		List<VendaCompraLojaVirtualDTO> virtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		// Percorre cada venda encontrada
		for (VendaCompraLojaVirtual vcl : vendaBuscada) {

			VendaCompraLojaVirtualDTO virtualDTO = new VendaCompraLojaVirtualDTO();

			// Copia dados principais da venda
			virtualDTO.setValorTotal(vcl.getValorTotal());
			virtualDTO.setPessoa(vcl.getPessoa());
			virtualDTO.setEntrega(vcl.getEnderecoEntrega());
			virtualDTO.setCobranca(vcl.getEnderecoCobranca());
			virtualDTO.setValorDesconto(vcl.getValorDesconto());
			virtualDTO.setId(vcl.getId());

			// Converte os itens da venda para DTO
			for (ItemVendaLoja itemVendaLoja : vcl.getItemVendaLojas()) {

				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();

				Produto produto = itemVendaLoja.getProduto();

				ProdutoDTO produtoDTO = new ProdutoDTO();
				produtoDTO.setId(produto.getId());
				produtoDTO.setNome(produto.getNome());
				produtoDTO.setValorVenda(produto.getValorVenda());

				itemVendaDTO.setProduto(produtoDTO);
				itemVendaDTO.setQuantidade(itemVendaLoja.getQuantidade());

				virtualDTO.getItemVendaDTOs().add(itemVendaDTO);
			}

			virtualDTOList.add(virtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(virtualDTOList, HttpStatus.OK);
	}

	@GetMapping(value = "/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(@PathVariable("valor") String valor,
			@PathVariable("tipoconsulta") String tipoconsulta) {

		List<VendaCompraLojaVirtual> vendaBuscada = null;

		if (tipoconsulta.equalsIgnoreCase("por_id_prod")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorProduto(Long.parseLong(valor));

		} else if (tipoconsulta.equalsIgnoreCase("por_nome_prod")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorNomeProduto(valor.toUpperCase().trim());

		} else if (tipoconsulta.equalsIgnoreCase("por_nome_cliente")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorNomeCliente(valor.toUpperCase().trim());

		} else if (tipoconsulta.equalsIgnoreCase("por_nome_cliente")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorNomeCliente(valor.toUpperCase().trim());

		} else if (tipoconsulta.equalsIgnoreCase("por_end_cobranca")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorEndCobranca(valor.toUpperCase().trim());

		} else if (tipoconsulta.equalsIgnoreCase("por_end_entrega")) {

			vendaBuscada = vd_cp_Loja_virtual_Repository.vendaPorEndEntrega(valor.toUpperCase().trim());

		}

		if (vendaBuscada == null) {
			vendaBuscada = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> virtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vcl : vendaBuscada) {

			VendaCompraLojaVirtualDTO virtualDTO = new VendaCompraLojaVirtualDTO();

			virtualDTO.setValorTotal(vcl.getValorTotal());
			virtualDTO.setPessoa(vcl.getPessoa());
			virtualDTO.setEntrega(vcl.getEnderecoEntrega());
			virtualDTO.setCobranca(vcl.getEnderecoCobranca());
			virtualDTO.setValorDesconto(vcl.getValorDesconto());
			virtualDTO.setId(vcl.getId());

			for (ItemVendaLoja itemVendaLoja : vcl.getItemVendaLojas()) {

				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();

				Produto produto = itemVendaLoja.getProduto();

				ProdutoDTO produtoDTO = new ProdutoDTO();
				produtoDTO.setId(produto.getId());
				produtoDTO.setNome(produto.getNome());
				produtoDTO.setValorVenda(produto.getValorVenda());

				itemVendaDTO.setProduto(produtoDTO);
				itemVendaDTO.setQuantidade(itemVendaLoja.getQuantidade());

				virtualDTO.getItemVendaDTOs().add(itemVendaDTO);
			}

			virtualDTOList.add(virtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(virtualDTOList, HttpStatus.OK);
	}

	@GetMapping(value = "/consultaFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaFaixaData(@PathVariable("data1") String data1,
			@PathVariable("data2") String data2) {

		List<VendaCompraLojaVirtual> vendaBuscada = null;

		java.sql.Date d1 = java.sql.Date.valueOf(data1); // "2026-02-25"
		java.sql.Date d2 = java.sql.Date.valueOf(data2); // "2026-02-27"

		List<VendaCompraLojaVirtual> vendas = vd_cp_Loja_virtual_Repository.consultaVendaPorData(d1, d2);

		vendaBuscada = vendas;

		if (vendaBuscada == null) {
			vendaBuscada = new ArrayList<VendaCompraLojaVirtual>();
		}

		List<VendaCompraLojaVirtualDTO> virtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

		for (VendaCompraLojaVirtual vcl : vendaBuscada) {

			VendaCompraLojaVirtualDTO virtualDTO = new VendaCompraLojaVirtualDTO();

			virtualDTO.setValorTotal(vcl.getValorTotal());
			virtualDTO.setPessoa(vcl.getPessoa());
			virtualDTO.setEntrega(vcl.getEnderecoEntrega());
			virtualDTO.setCobranca(vcl.getEnderecoCobranca());
			virtualDTO.setValorDesconto(vcl.getValorDesconto());
			virtualDTO.setId(vcl.getId());

			for (ItemVendaLoja itemVendaLoja : vcl.getItemVendaLojas()) {

				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();

				Produto produto = itemVendaLoja.getProduto();

				ProdutoDTO produtoDTO = new ProdutoDTO();
				produtoDTO.setId(produto.getId());
				produtoDTO.setNome(produto.getNome());
				produtoDTO.setValorVenda(produto.getValorVenda());

				itemVendaDTO.setProduto(produtoDTO);
				itemVendaDTO.setQuantidade(itemVendaLoja.getQuantidade());

				virtualDTO.getItemVendaDTOs().add(itemVendaDTO);
			}

			virtualDTOList.add(virtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(virtualDTOList, HttpStatus.OK);
	}

	@PostMapping("/consultaFrete")
	public ResponseEntity<List<EmpresaTransporteDTO>> consultaFrete(@RequestBody @Valid ConsutaFreteDTO freteDTO)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(freteDTO);

		MediaType JSON = MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(json, JSON);

		OkHttpClient client = new OkHttpClient();

		String url = ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/calculate";

		Request request = new Request.Builder().url(url) // Endpoint do cálculo de frete
				.post(body) // Envia o JSON no corpo
				.addHeader("Accept", "application/json") // Espera resposta JSON
				.addHeader("Content-Type", "application/json") // Envia JSON
				.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO) // Token de
																										// acesso
				.addHeader("User-Agent", "suporte@manel.com.br") // Identificação do cliente
				.build();

		Response resposta = client.newCall(request).execute();

		JsonNode jsonNode = new ObjectMapper().readTree(resposta.body().string());

		List<EmpresaTransporteDTO> transporteDTOs = new ArrayList<EmpresaTransporteDTO>();

		for (JsonNode node : jsonNode) {
			EmpresaTransporteDTO dto = new EmpresaTransporteDTO();

			if (node.get("id") != null) {

				dto.setId(node.get("id").asText());
			}
			if (node.get("name") != null) {
				dto.setName(node.get("name").asText());
			}
			if (node.get("price") != null) {
				dto.setPrice(node.get("price").asText());
			}
			if (node.get("company") != null) {
				dto.setCompany(node.get("company").get("name").asText());
				dto.setPicture(node.get("company").get("picture").asText());

			}
			if (dto.dadosOk()) {
				transporteDTOs.add(dto);
			}
		}

		return new ResponseEntity<List<EmpresaTransporteDTO>>(transporteDTOs, HttpStatus.OK);

	}

	@PostMapping("/gerarEtiqueta/{idVenda}")
	public ResponseEntity<String> gerarEtiqueta(@PathVariable("idVenda") Long idVenda)
			throws ExcepetionLojaVirtual, IOException {

		VendaCompraLojaVirtual compraLojaVirtual = vd_cp_Loja_virtual_Repository.findById(idVenda).orElseGet(null);

		
		
		if (compraLojaVirtual == null) {

			return new ResponseEntity<String>("Venda não encontrada", HttpStatus.OK);
		}

		EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();

		envioEtiquetaDTO.setAgency("49");
		envioEtiquetaDTO.setService(Integer.valueOf(compraLojaVirtual.getServicoTransportadora()));
		
		envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
		envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
		envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
		envioEtiquetaDTO.getFrom().setCompanyDocument(compraLojaVirtual.getEmpresa().getCnpj());
		envioEtiquetaDTO.getFrom().setStateRegister(compraLojaVirtual.getEmpresa().getInscEstadual());
		envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getRuaLogra());
		envioEtiquetaDTO.getFrom()
				.setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemtento());
		envioEtiquetaDTO.getFrom().setNumber(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
		envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getBairro());
		envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
		envioEtiquetaDTO.getFrom().setPostalCode(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
		envioEtiquetaDTO.getFrom().setStateAbbr(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());

		envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoa().getNome());
		envioEtiquetaDTO.getTo().setEmail(compraLojaVirtual.getPessoa().getEmail());
		envioEtiquetaDTO.getTo().setPhone(compraLojaVirtual.getPessoa().getTelefone());
		envioEtiquetaDTO.getTo().setDocument(compraLojaVirtual.getPessoa().getCpf());
		envioEtiquetaDTO.getTo().setAddress(compraLojaVirtual.getPessoa().enderecoEntrega().getRuaLogra());
		envioEtiquetaDTO.getTo().setComplement(compraLojaVirtual.getPessoa().enderecoEntrega().getComplemtento());
		envioEtiquetaDTO.getTo().setNumber(compraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
		envioEtiquetaDTO.getTo().setDistrict(compraLojaVirtual.getPessoa().enderecoEntrega().getBairro());
		envioEtiquetaDTO.getTo().setCity(compraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
		envioEtiquetaDTO.getTo().setPostalCode(compraLojaVirtual.getPessoa().enderecoEntrega().getCep());
		envioEtiquetaDTO.getTo().setStateAbbr(compraLojaVirtual.getPessoa().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setCountryId("BR");

		List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();

		for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

			ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();

			dto.setName(itemVendaLoja.getProduto().getNome());
			dto.setQuantity(itemVendaLoja.getQuantidade().toString());
			dto.setUnitaryValue(itemVendaLoja.getProduto().getValorVenda());			
			products.add(dto);

		}

		envioEtiquetaDTO.setProducts(products);

		List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

		for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

			VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();

			dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
			dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
			dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
			dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());
			volumes.add(dto);
		}

		envioEtiquetaDTO.setVolumes(volumes);

		envioEtiquetaDTO.getOptions().setInsuranceValue("1.00");

		envioEtiquetaDTO.getOptions().setReceipt(false);
		envioEtiquetaDTO.getOptions().setOwnHand(false);
		envioEtiquetaDTO.getOptions().setReverse(false);
		envioEtiquetaDTO.getOptions().setReminder("Compra tal");
		envioEtiquetaDTO.getOptions().setPlatform(compraLojaVirtual.getEmpresa().getNomeFantasia());
		//envioEtiquetaDTO.getOptions().getInvoice().setKey(compraLojaVirtual.getNotaFiscalVenda().getNumeroNota());

		String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);

		
		
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
		Request request = new Request.Builder().url("https://sandbox.melhorenvio.com.br/api/v2/me/cart").post(body)
				.addHeader("Accept", "application/json").addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
				.addHeader("User-Agent", "eu@eu.com").build();

		Response resposta = client.newCall(request).execute();

		String respostaCarrinho = resposta.body().string();
		System.out.println("RESPOSTA CARRINHO = " + respostaCarrinho);

		System.out.println("JSON ENVIO = " + jsonEnvio);

		JsonNode jsonNode = new ObjectMapper().readTree(respostaCarrinho);

		String idEtiqueta = jsonNode.get("id").asText();
		System.out.println("ID ETIQUETA = " + idEtiqueta);

		compraLojaVirtual.setCodigoEtiqueta(idEtiqueta);

		// salvando codigo da etiqueta
		vd_cp_Loja_virtual_Repository.updateEtiqueta(idEtiqueta, compraLojaVirtual.getId());

		/* =========================
		 * CHECKOUT
		 * ========================= */
		OkHttpClient clientCompra = new OkHttpClient();

		MediaType mediaTypeC = MediaType.parse("application/json");

		String jsonCheckout = """
		        {
		          "orders": [
		            "%s"
		          ]
		        }
		        """.formatted(idEtiqueta);

		okhttp3.RequestBody bodyC = okhttp3.RequestBody.create(mediaTypeC, jsonCheckout);

		Request requestC = new Request.Builder()
		        .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/checkout")
		        .post(bodyC)
		        .addHeader("Accept", "application/json")
		        .addHeader("Content-Type", "application/json")
		        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
		        .addHeader("User-Agent", "eu@eu.com.br")
		        .build();

		Response responseC = clientCompra.newCall(requestC).execute();
		String respostaCheckout = responseC.body().string();

		System.out.println("JSON CHECKOUT = " + jsonCheckout);
		System.out.println("RESPOSTA CHECKOUT = " + respostaCheckout);

		if (!responseC.isSuccessful()) {
		    return new ResponseEntity<String>(respostaCheckout, HttpStatus.OK);
		}

		/* =========================
		 * GENERATE
		 * ========================= */
		OkHttpClient clientGera = new OkHttpClient();

		MediaType mediaTypeG = MediaType.parse("application/json");

		String jsonGenerate = """
		        {
		          "orders": [
		            "%s"
		          ]
		        }
		        """.formatted(idEtiqueta);

		okhttp3.RequestBody bodyG = okhttp3.RequestBody.create(mediaTypeG, jsonGenerate);

		Request requestG = new Request.Builder()
		        .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/generate")
		        .post(bodyG)
		        .addHeader("Accept", "application/json")
		        .addHeader("Content-Type", "application/json")
		        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
		        .addHeader("User-Agent", "eu@eu.com.br")
		        .build();

		Response responseG = clientGera.newCall(requestG).execute();
		String respostaGenerate = responseG.body().string();

		System.out.println("JSON GENERATE = " + jsonGenerate);
		System.out.println("RESPOSTA GENERATE = " + respostaGenerate);

		if (!responseG.isSuccessful()) {
		    return new ResponseEntity<String>(respostaGenerate, HttpStatus.OK);
		}

		/* =========================
		 * PRINT
		 * ========================= */
		OkHttpClient clientImprime = new OkHttpClient();

		MediaType mediaTypeI = MediaType.parse("application/json");

		String jsonPrint = """
		        {
		          "mode": "private",
		          "orders": [
		            "%s"
		          ]
		        }
		        """.formatted(idEtiqueta);

		okhttp3.RequestBody bodyI = okhttp3.RequestBody.create(mediaTypeI, jsonPrint);

		Request requestI = new Request.Builder()
		        .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/print")
		        .post(bodyI)
		        .addHeader("Accept", "application/json")
		        .addHeader("Content-Type", "application/json")
		        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
		        .addHeader("User-Agent", "eu@eu.com")
		        .build();

		Response responseI = clientImprime.newCall(requestI).execute();
		String respostaPrint = responseI.body().string();

		System.out.println("JSON PRINT = " + jsonPrint);
		System.out.println("RESPOSTA PRINT = " + respostaPrint);

		if (!responseI.isSuccessful()) {
		    return new ResponseEntity<String>(respostaPrint, HttpStatus.OK);
		}

		/* =========================
		 * SALVAR URL / RETORNO
		 * ========================= */
		String urlEtiqueta = respostaPrint;

		vd_cp_Loja_virtual_Repository.updateURLEtiqueta(urlEtiqueta, compraLojaVirtual.getId());
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);

}
	
	@PostMapping("/cancelaEtiqueta/{idEtiqueta}/{descricao}")
	public ResponseEntity<String> cancelaEtiqueta(@PathVariable String idEtiqueta,@PathVariable String descricao) throws IOException{
		
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");

		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, """
				{
				  "id": "%s",
				  "description": "%s"
				}
				""".formatted(idEtiqueta, descricao));		
		
		Request request = new Request.Builder()
		  .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/cancel")
		  .post(body)
		  .addHeader("accept", "application/json")
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer token")
		  .addHeader("User-Agent", "eu@ri.com.br")
		  .build();

		Response response = client.newCall(request).execute();
		

		
		
		
		
		return new ResponseEntity<String>("Cancelado com sucesso",HttpStatus.OK) ;
		
		
	}
	
	
	@PostMapping("/statusRastreio/{idVenda}/{idEtiqueta}")
	public ResponseEntity<String> statusRastreio (@PathVariable Long idVenda,@PathVariable String idEtiqueta) throws IOException {
		
		
		OkHttpClient clientRas = new OkHttpClient();

		MediaType mediaTypeRas = MediaType.parse("application/json");
		
		String jsonRastreio = """
		        {
		          "orders": [
		            "%s"
		          ]
		        }
		        """.formatted(idEtiqueta);
		
		okhttp3.RequestBody bodyRas = okhttp3.RequestBody.create(mediaTypeRas, jsonRastreio);
		
		Request requestRas = new Request.Builder()
		  .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/tracking")
		  .post((okhttp3.RequestBody) bodyRas)
		  .addHeader("accept", "application/json")
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-type", "application/json")
		  .addHeader("Authorization", "Bearer "+ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
		  .addHeader("User-Agent", "eu@eu.com")
		  .build();

		Response responseRas = clientRas.newCall(requestRas).execute();
		
		
		String respostaRastreio = responseRas.body().string();
		System.out.println("Rastreio = " + respostaRastreio);

		System.out.println("RESPOSTA CARRINHO = " + respostaRastreio);


		JsonNode jsonNode = new ObjectMapper().readTree(respostaRastreio);

		JsonNode objetoEtiqueta = jsonNode.get(idEtiqueta);

		String tracking = objetoEtiqueta.get("tracking").asText();
		String melhorEnvioTracking = objetoEtiqueta.get("melhorenvio_tracking").asText();

		System.out.println("TRACKING TRANSPORTADORA = " + tracking);
		System.out.println("TRACKING MELHOR ENVIO = " + melhorEnvioTracking);
		
		VendaCompraLojaVirtual compraLojaVirtual = new VendaCompraLojaVirtual();
		
		List<StatusRastreio>  rastreios = statusRastreioRepository.listaRastreioVenda(idVenda);

		if (!rastreios.isEmpty()) {
		    StatusRastreio rastreio = rastreios.get(0);
		    rastreio.setUrlRastreio(melhorEnvioTracking);
		    statusRastreioRepository.save(rastreio);
		}
		
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
	}
	
	@PostMapping("/gerarBoletoPix")
	public ResponseEntity<String> gerarBoletoPix(@RequestBody @Valid ObjetoPostCarneJuno objetoPostCarneJuno)
			throws Exception{
		
		
		
		return new ResponseEntity<String>(serviceJunoBoleto.gerarCarneApi(objetoPostCarneJuno),HttpStatus.OK);		
		
	}
	
	@PostMapping("/cancelarBoletoPix")
	public ResponseEntity<String> cancelarBoletoPix(@RequestBody @Valid String code)
			throws Exception{
		
		
		
		return new ResponseEntity<String>(serviceJunoBoleto.cancelarBoleto(code),HttpStatus.OK);		
		
	}
	
	
	
}
