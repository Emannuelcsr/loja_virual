package jdev.mentoria.lojavirtual.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.CategoriaProduto;
import jdev.mentoria.lojavirtual.model.dto.CategoriaProdutoDTO;
import jdev.mentoria.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;


    // -------------------------------------------------------------------------
    // MÉTODO: salvarCategoria
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Salva uma nova categoria de produto ou atualiza uma categoria existente.
    //
    // FLUXO:
    // 1. Recebe uma CategoriaProduto vinda do Angular.
    // 2. Verifica se a empresa foi informada.
    // 3. Verifica se já existe categoria com o mesmo nome.
    // 4. Salva no banco.
    // 5. Retorna um DTO com os dados principais da categoria salva.
    //
    // ENDPOINT:
    // POST /salvarcategoria
    // -------------------------------------------------------------------------

    @PostMapping(value = "/salvarcategoria")
    public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(
            @RequestBody CategoriaProduto categoriaProduto) throws ExcepetionLojaVirtual {

        if (categoriaProduto.getEmpresa() == null || categoriaProduto.getEmpresa().getId() == null) {
            throw new ExcepetionLojaVirtual("Informe a empresa");
        }

        if (categoriaProduto.getId() == null
                && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase().trim())) {

            throw new ExcepetionLojaVirtual("Não pode cadastrar categoria com mesmo nome");
        }

        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

        CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

        categoriaProdutoDTO.setId(categoriaSalva.getId());
        categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
        categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

        return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: deletecategoria
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Remove uma categoria de produto do banco.
    //
    // FLUXO:
    // 1. Recebe uma CategoriaProduto vinda do Angular.
    // 2. Verifica se o ID existe no banco.
    // 3. Se não existir, lança uma exceção.
    // 4. Se existir, remove a categoria.
    // 5. Retorna uma mensagem de sucesso em JSON.
    //
    // ENDPOINT:
    // POST /deletecategoria
    // -------------------------------------------------------------------------

    @ResponseBody
    @PostMapping(value = "/deletecategoria")
    public ResponseEntity<?> deletecategoria(
            @RequestBody CategoriaProduto categoriaProduto) throws ExcepetionLojaVirtual {

        if (!categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent()) {
            throw new ExcepetionLojaVirtual("Categoria já foi Removida");
        }

        categoriaProdutoRepository.deleteById(categoriaProduto.getId());

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Categoria Removida");

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: buscarPorCatDescricao
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Busca categorias pela descrição, sem filtrar por empresa.
    //
    // FLUXO:
    // 1. Recebe uma descrição pela URL.
    // 2. Converte a descrição para letra maiúscula.
    // 3. Consulta no banco categorias com essa descrição.
    // 4. Retorna uma lista direta de CategoriaProduto.
    //
    // ENDPOINT:
    // GET /buscarPorCatDescricao/{desc}
    //
    // RETORNO JSON:
    // [
    //   { "id": 1, "nomeDesc": "BEBIDAS" }
    // ]
    // -------------------------------------------------------------------------

    @GetMapping(value = "/buscarPorCatDescricao/{desc}")
    public ResponseEntity<List<CategoriaProduto>> buscarPorCatDescricao(
            @PathVariable("desc") String desc) {

        List<CategoriaProduto> categoriaProduto =
                categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase());

        return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: buscarPorCatDescricaoPorEmpresa
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Busca categorias pela descrição e pela empresa.
    //
    // FLUXO:
    // 1. Recebe a descrição pela URL.
    // 2. Recebe a empresa pela URL.
    // 3. Busca no banco categorias daquela empresa com aquela descrição.
    // 4. Monta um Map com:
    //    - resposta: lista de categorias encontradas
    //    - total: quantidade de categorias encontradas
    // 5. Retorna esse Map em formato JSON.
    //
    // ENDPOINT:
    // GET /buscarPorCatDescricaoPorEmpresa/{desc}/{empresa}
    //
    // RETORNO JSON:
    // {
    //   "resposta": [
    //     { "id": 1, "nomeDesc": "BEBIDAS" }
    //   ],
    //   "total": 1
    // }
    // -------------------------------------------------------------------------

    @GetMapping(value = "/buscarPorCatDescricaoPorEmpresa/{desc}/{empresa}")
    public ResponseEntity<Map<String, Object>> buscarPorCatDescricaoPorEmpresa(
            @PathVariable("desc") String desc,
            @PathVariable("empresa") String empresa) {

        List<CategoriaProduto> categoriaProduto =
                categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase(), empresa);

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("resposta", categoriaProduto);
        resposta.put("total", categoriaProduto.size());

        return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: buscarporid
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Busca uma categoria específica pelo ID.
    //
    // FLUXO:
    // 1. Recebe o ID pela URL.
    // 2. Procura a categoria no banco.
    // 3. Coloca a categoria dentro de um Map com a chave "resposta".
    // 4. Retorna a categoria em JSON.
    //
    // ENDPOINT:
    // GET /buscarporid/{id}
    //
    // RETORNO JSON:
    // {
    //   "resposta": {
    //     "id": 1,
    //     "nomeDesc": "BEBIDAS"
    //   }
    // }
    // -------------------------------------------------------------------------

    @GetMapping(value = "/buscarporid/{id}")
    public ResponseEntity<Map<String, CategoriaProduto>> buscarporid(
            @PathVariable("id") Long id) {

        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).get();

        Map<String, CategoriaProduto> resposta = new HashMap<>();

        resposta.put("resposta", categoriaProduto);

        return new ResponseEntity<Map<String, CategoriaProduto>>(resposta, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: listarCategoriaProduto
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Lista todas as categorias de produto de uma empresa.
    //
    // FLUXO:
    // 1. Recebe o código da empresa pela URL.
    // 2. Busca todas as categorias daquela empresa.
    // 3. Retorna uma lista direta de CategoriaProduto.
    //
    // ENDPOINT:
    // GET /listarcategoriaproduto/{codEmp}
    //
    // RETORNO JSON:
    // [
    //   { "id": 1, "nomeDesc": "BEBIDAS" },
    //   { "id": 2, "nomeDesc": "ROUPAS" }
    // ]
    // -------------------------------------------------------------------------

    @GetMapping(value = "/listarcategoriaproduto/{codEmp}")
    public ResponseEntity<List<CategoriaProduto>> listarCategoriaProduto(
            @PathVariable("codEmp") Long codEmp) {

        List<CategoriaProduto> categoriaProduto =
                categoriaProdutoRepository.findAll(codEmp);

        return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: qtdadePaginaCategoriaProduto
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Retorna a quantidade de páginas de categorias de uma empresa.
    //
    // FLUXO:
    // 1. Recebe o código da empresa pela URL.
    // 2. Consulta no banco a quantidade de páginas.
    // 3. Coloca esse número dentro de um Map.
    // 4. Retorna o Map em JSON.
    //
    // ENDPOINT:
    // GET /qtdadePaginaCategoriaProduto/{codEmp}
    //
    // RETORNO JSON:
    // {
    //   "resposta": 3
    // }
    // -------------------------------------------------------------------------

    @GetMapping(value = "/qtdadePaginaCategoriaProduto/{codEmp}")
    public ResponseEntity<Map<String, Integer>> qtdadePaginaCategoriaProduto(
            @PathVariable("codEmp") Long codEmp) {

        Integer qtdadePagina =
                categoriaProdutoRepository.quantidadePagina(codEmp);

        Map<String, Integer> resposta = new HashMap<>();

        resposta.put("resposta", qtdadePagina);

        return new ResponseEntity<Map<String, Integer>>(resposta, HttpStatus.OK);
    }


    // -------------------------------------------------------------------------
    // MÉTODO: listaPorPageCategoriaProduto
    // -------------------------------------------------------------------------
    // O QUE FAZ:
    // Lista categorias de produto usando paginação.
    //
    // FLUXO:
    // 1. Recebe o código da empresa pela URL.
    // 2. Recebe o número da página pela URL.
    // 3. Cria um Pageable com:
    //    - página desejada
    //    - 5 registros por página
    //    - ordenação por nomeDesc
    // 4. Busca a lista paginada no banco.
    // 5. Retorna a lista da página atual.
    //
    // ENDPOINT:
    // GET /listaPorPageCategoriaProduto/{codEmp}/{pagina}
    //
    // RETORNO JSON:
    // [
    //   { "id": 1, "nomeDesc": "BEBIDAS" },
    //   { "id": 2, "nomeDesc": "ROUPAS" }
    // ]
    //
    // OBSERVAÇÃO:
    // Esse método retorna apenas os registros da página atual.
    // Ele não retorna o total geral de registros.
    // -------------------------------------------------------------------------

    @GetMapping(value = "/listaPorPageCategoriaProduto/{codEmp}/{pagina}")
    public ResponseEntity<List<CategoriaProduto>> listaPorPageCategoriaProduto(
            @PathVariable("codEmp") Long codEmp,
            @PathVariable("pagina") Integer pagina) {

        org.springframework.data.domain.Pageable pageable =
                PageRequest.of(pagina - 1, 5, Sort.by("nomeDesc"));

        List<CategoriaProduto> lista =
                categoriaProdutoRepository.findbyPage(codEmp, pageable);

        return new ResponseEntity<List<CategoriaProduto>>(lista, HttpStatus.OK);
    }
    
    @GetMapping(value = "/quantidadeDeCategorias/{codEmp}")
    public ResponseEntity<Integer> quantidadeDeCategorias(@PathVariable("codEmp")Long codEmp){
    	
    	Integer quantidadeTotal= categoriaProdutoRepository.findAll(codEmp).size();
    	
    	return new ResponseEntity<Integer>(quantidadeTotal,HttpStatus.OK);
    }
    
    
}