package jdev.mentoria.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Put;
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

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.ItemVendaLoja;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.Produto;
import jdev.mentoria.lojavirtual.model.StatusRastreio;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.ItemVendaDTO;
import jdev.mentoria.lojavirtual.model.dto.ProdutoDTO;
import jdev.mentoria.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.mentoria.lojavirtual.repository.StatusRastreioRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;

@RestController
public class Vc_Cp_Loja_Virtual_Controller {

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

	@PostMapping(value = "/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExcepetionLojaVirtual {

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

		StatusRastreio statusRastreio = new StatusRastreio();

		statusRastreio.setCentroDistribuicao("Loja local");
		statusRastreio.setCidade("Local");
		statusRastreio.setEmpresa(vendaSalva.getEmpresa());
		statusRastreio.setEstado("Local");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaSalva);

		statusRastreioRepository.save(statusRastreio);

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
	 * (convertidas para DTO) que possuem esse produto em seus itens.
	 * Apenas vendas não excluídas (exclusão lógica) são consideradas.
	 * </p>
	 *
	 * @param idProduto ID do produto utilizado para filtrar as vendas
	 * @return ResponseEntity contendo lista de VendaCompraLojaVirtualDTO
	 *         com status HTTP 200 (OK)
	 */
	@GetMapping(value = "/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(
	        @PathVariable("id") Long idProduto) {

		
		
	    // Busca no repository todas as vendas que possuem o produto informado
	    List<VendaCompraLojaVirtual> vendaBuscada =
	            vd_cp_Loja_virtual_Repository.vendaPorProduto(idProduto);

	    
	    
	    
	    
	    // Garante que a lista não seja null (Spring normalmente retorna lista vazia)
	    if (vendaBuscada == null) {
	        vendaBuscada = new ArrayList<VendaCompraLojaVirtual>();
	    }

	    
	    
	    
	    // Lista que será retornada ao cliente (DTO)
	    List<VendaCompraLojaVirtualDTO> virtualDTOList =
	            new ArrayList<VendaCompraLojaVirtualDTO>();

	    
	    
	    
	    // Percorre cada venda encontrada
	    for (VendaCompraLojaVirtual vcl : vendaBuscada) {

	        VendaCompraLojaVirtualDTO virtualDTO =
	                new VendaCompraLojaVirtualDTO();

	        
	        
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

	    return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(
	            virtualDTOList, HttpStatus.OK);
	}

}
