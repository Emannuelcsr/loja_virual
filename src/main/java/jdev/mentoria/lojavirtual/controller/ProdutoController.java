package jdev.mentoria.lojavirtual.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.Produto;
import jdev.mentoria.lojavirtual.repository.ProdutoRepository;
import jdev.mentoria.lojavirtual.service.SendEmailService;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private SendEmailService sendEmailService;

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto)
			throws ExcepetionLojaVirtual, MessagingException, IOException {

		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {

			throw new ExcepetionLojaVirtual("O tipo de unidade deve ser informada");
		}

		if (produto.getNome().length() < 10) {

			throw new ExcepetionLojaVirtual("Nome do produto dever ter no minimo 10 letras");
		}

		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("A empresa responsável deve ser informada");
		}

		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(),
					produto.getEmpresa().getId());

			if (!produtos.isEmpty()) {
				throw new ExcepetionLojaVirtual("Já existe produto com o nome: " + produto.getNome());
			}
		}

		if (produto.getQuantidadeEstoque() <= 1) {

			throw new ExcepetionLojaVirtual("O produto deve ter no minimo 1 no estoque");
		}

		if (produto.getImagemProdutos() == null || produto.getImagemProdutos().isEmpty()
				|| produto.getImagemProdutos().size() == 0) {

			throw new ExcepetionLojaVirtual("Selecione imagens ao produto");

		}

		if (produto.getImagemProdutos().size() < 3) {

			throw new ExcepetionLojaVirtual("Deve ser informado ao menos 3 imagens para o produto");

		}

		if (produto.getImagemProdutos().size() > 6) {

			throw new ExcepetionLojaVirtual("Deve ser informado ao maximo 6 imagens para o produto");

		}
		if (produto.getAlertaQuantidadeEstoque() && produto.getQuantidadeEstoque() <= 1) {

			StringBuilder html = new StringBuilder();

			html.append("<h2>").append("Produto: " + produto.getNome())
					.append(" com estoque baixo: " + produto.getQuantidadeEstoque()).append("</h2>");

			html.append("<p>Id do produto: ").append(produto.getId()).append("</p>");

			if (produto.getEmpresa().getEmail() != null) {
				sendEmailService.enviarEmailHtml("Produto sem estoque ", html.toString(),
						produto.getEmpresa().getEmail());

			}

		}

		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Categoria deve ser informada");
		}

		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Marca deve ser informada");
		}

		
		
		
		/**
		 * Processa as imagens de um produto novo (ainda sem ID),
		 * gera miniaturas redimensionadas e vincula corretamente
		 * cada imagem ao produto e à empresa.
		 *
		 * Regras aplicadas:
		 * - Só executa se o produto ainda não foi salvo (ID == null)
		 * - Converte Base64 → bytes → BufferedImage
		 * - Redimensiona para 800x600
		 * - Gera miniatura em Base64 (PNG)
		 */
		if (produto.getId() == null) {

		    // Percorre todas as imagens associadas ao produto
		    for (int x = 0; x < produto.getImagemProdutos().size(); x++) {

		        /*
		         * Vincula a imagem ao produto atual.
		         * Isso é essencial para o Hibernate entender o relacionamento.
		         */
		        produto.getImagemProdutos().get(x).setProduto(produto);

		        /*
		         * Define a empresa da imagem.
		         * Mantém consistência de dados (imagem pertence à mesma empresa do produto).
		         */
		        produto.getImagemProdutos().get(x).setEmpresa(produto.getEmpresa());

		        String base64image = "";

		        // Obtém a string Base64 original da imagem
		        String original = produto.getImagemProdutos().get(x).getImagemOriginal();

		        /*
		         * Verifica se a imagem veio no formato:
		         * data:image/png;base64,XXXXX
		         *
		         * Se sim → remove o prefixo e pega só o Base64 puro
		         * Se não → assume que já é Base64 puro
		         */
		        if (original != null && original.contains("data:image")) {
		            base64image = original.split(",")[1];
		        } else {
		            base64image = original;
		        }

		        /*
		         * Decodifica Base64 → array de bytes
		         * Agora temos os dados binários reais da imagem
		         */
		        byte[] imagemBytes = Base64.getDecoder().decode(base64image);

		        /*
		         * Converte bytes → BufferedImage
		         * Isso cria uma imagem manipulável em memória
		         */
		        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemBytes));

		        // Só continua se a imagem foi carregada corretamente
		        if (bufferedImage != null) {

		            /*
		             * Define o tipo da nova imagem.
		             * Se getType() == 0 → usa ARGB (evita erro de transparência)
		             */
		            int type = bufferedImage.getType() == 0
		                    ? BufferedImage.TYPE_INT_ARGB
		                    : bufferedImage.getType();

		            // Dimensões fixas da miniatura
		            int largura = 800;
		            int altura = 600;

		            /*
		             * Cria uma nova imagem vazia já no tamanho desejado
		             */
		            BufferedImage resizedImage = new BufferedImage(largura, altura, type);

		            /*
		             * Graphics2D permite desenhar/redimensionar imagens
		             */
		            Graphics2D graphics2d = resizedImage.createGraphics();

		            /*
		             * Desenha a imagem original dentro da nova imagem redimensionada
		             */
		            graphics2d.drawImage(bufferedImage, 0, 0, largura, altura, null);

		            // Libera recursos gráficos
		            graphics2d.dispose();

		            /*
		             * Converte BufferedImage → bytes (PNG)
		             */
		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            ImageIO.write(resizedImage, "png", outputStream);

		            /*
		             * Converte bytes → Base64
		             * Prefixo data:image/png;base64 permite exibir direto no navegador
		             */
		            String miniImgBase64 = "data:image/png;base64,"
		                    + Base64.getEncoder().encodeToString(outputStream.toByteArray());

		            // Salva a miniatura no objeto
		            produto.getImagemProdutos().get(x).setImagemMiniatura(miniImgBase64);

		            // Limpeza opcional de memória
		            bufferedImage.flush();
		            resizedImage.flush();
		            outputStream.close();
		        }
		    }
		}

		Produto produtoSalvo = produtoRepository.save(produto);

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {// requestBody transforma JSON da tela em
																			// objeto

		produtoRepository.deleteById(produto.getId());

		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteProdutoPorId/{id}")
	public ResponseEntity<?> deleteProdutoPorId(@PathVariable("id") Long id) {

		produtoRepository.deleteById(id);

		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}

	@GetMapping(value = "/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {

			throw new ExcepetionLojaVirtual("Não encontou produto com código: " + id);
		}

		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@GetMapping(value = "/buscarProdutoPorNome/{desc}")
	public ResponseEntity<List<Produto>> buscarPorDescricao(@PathVariable("desc") String desc) {

		List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase());

		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
	}

}
