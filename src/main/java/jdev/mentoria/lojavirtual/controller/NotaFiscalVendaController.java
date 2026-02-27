package jdev.mentoria.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.NotaFiscalVenda;
import jdev.mentoria.lojavirtual.model.dto.NotaFiscalVendaDTO;
import jdev.mentoria.lojavirtual.repository.NotaFiscalVendaRepository;

@RestController
public class NotaFiscalVendaController {

	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;

	@GetMapping(value = "/obterNotaFiscalCompraDaVenda/{idVenda}")
	public ResponseEntity<List<NotaFiscalVendaDTO>> obterNotaFiscalCompraDaVenda(@PathVariable("idVenda") Long idVenda)
			throws ExcepetionLojaVirtual {

		List<NotaFiscalVenda> NotaFiscalBuscada = null;

		NotaFiscalBuscada = notaFiscalVendaRepository.buscaNotaPorVenda(idVenda);
		if (NotaFiscalBuscada.isEmpty() ){

			throw new ExcepetionLojaVirtual("Não encontou nota fiscal de compra  da Venda com código: " + idVenda);
		}

		List<NotaFiscalVendaDTO> NotaFiscalListaDTO = new ArrayList<NotaFiscalVendaDTO>();

		for (NotaFiscalVenda NFDTO : NotaFiscalBuscada) {

			NotaFiscalVendaDTO notaFiscalVendaDTO = new NotaFiscalVendaDTO();

			notaFiscalVendaDTO.setId(NFDTO.getId());
			notaFiscalVendaDTO.setEmpresaId(NFDTO.getEmpresa().getId());
			notaFiscalVendaDTO.setNumeroNota(NFDTO.getNumeroNota());
			notaFiscalVendaDTO.setTipoNota(NFDTO.getTipoNota());

			NotaFiscalListaDTO.add(notaFiscalVendaDTO);
		}

	

		return new ResponseEntity<List<NotaFiscalVendaDTO>>(NotaFiscalListaDTO, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/obterNotaFiscalCompraDaVendaUnico/{idVenda}")
	public ResponseEntity<List<NotaFiscalVendaDTO>> obterNotaFiscalCompraDaVendaUnico(@PathVariable("idVenda") Long idVenda)
			throws ExcepetionLojaVirtual {

		NotaFiscalVenda NotaFiscalBuscada = new NotaFiscalVenda();

		NotaFiscalBuscada = notaFiscalVendaRepository.buscaNotaPorVendaUnica(idVenda);
		
		if (NotaFiscalBuscada == null ){

			throw new ExcepetionLojaVirtual("Não encontou nota fiscal de compra  da Venda com código: " + idVenda);
		}

		List<NotaFiscalVendaDTO> NotaFiscalListaDTO = new ArrayList<NotaFiscalVendaDTO>();

	

			NotaFiscalVendaDTO notaFiscalVendaDTO = new NotaFiscalVendaDTO();

			notaFiscalVendaDTO.setId(NotaFiscalBuscada.getId());
			notaFiscalVendaDTO.setEmpresaId(NotaFiscalBuscada.getEmpresa().getId());
			notaFiscalVendaDTO.setNumeroNota(NotaFiscalBuscada.getNumeroNota());
			notaFiscalVendaDTO.setTipoNota(NotaFiscalBuscada.getTipoNota());

			NotaFiscalListaDTO.add(notaFiscalVendaDTO);
		

	

		return new ResponseEntity<List<NotaFiscalVendaDTO>>(NotaFiscalListaDTO, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
