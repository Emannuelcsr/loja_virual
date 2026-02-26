package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.NotaFiscalVenda;

@RestController
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

}
