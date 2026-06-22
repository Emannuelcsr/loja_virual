package jdev.mentoria.lojavirtual.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jdev.mentoria.lojavirtual.AsaasConfig;
import jdev.mentoria.lojavirtual.LojaVirualApplication;
import jdev.mentoria.lojavirtual.model.BoletoJuno;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.AsaasApiPagamentoStatus;
import jdev.mentoria.lojavirtual.model.dto.CartaoCreditoApiAsaas;
import jdev.mentoria.lojavirtual.model.dto.CartaoCreditoApiAsaasHolderInfo;
import jdev.mentoria.lojavirtual.model.dto.CobrancaApiAsaasCartao;
import jdev.mentoria.lojavirtual.model.dto.ErroResponseApiAsaasCartaoCredito;
import jdev.mentoria.lojavirtual.model.dto.ObjetoPostCarneJuno;
import jdev.mentoria.lojavirtual.model.dto.ResponseApiAsaasCartaoCreditoDTO;
import jdev.mentoria.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojavirtual.repository.BoletoJunoRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;
import jdev.mentoria.lojavirtual.service.HostIgnoreClient;
import jdev.mentoria.lojavirtual.service.ServiceAsaasBoleto;
import jdev.mentoria.lojavirtual.service.VendaService;
import jdev.mentoria.lojavirtual.util.ValidadorCPF;

/**
 * Controller responsável por abrir a tela de pagamento da venda.
 *
 * <p>
 * Função dessa classe no sistema:
 * </p>
 * <p>
 * Ela recebe a requisição da URL de pagamento, busca a venda no banco pelo id
 * informado na rota e envia os dados dessa venda para a página "pagamento".
 * </p>
 *
 * <p>
 * Em outras palavras:
 * </p>
 * <p>
 * Essa classe é a ponte entre a URL acessada no navegador e a tela HTML que o
 * usuário vai ver.
 * </p>
 *
 * <p>
 * Exemplo prático:
 * </p>
 * <p>
 * Se o usuário acessar:
 * </p>
 * 
 * <pre>
 * /pagamento/10
 * </pre>
 *
 * <p>
 * esse controller vai:
 * </p>
 * <ol>
 * <li>pegar o número 10 da URL</li>
 * <li>buscar a venda de id 10 no banco</li>
 * <li>converter essa venda para DTO</li>
 * <li>mandar esses dados para a tela "pagamento"</li>
 * </ol>
 *
 * <p>
 * Resumo simples:
 * </p>
 * <p>
 * Essa classe faz a tela de pagamento funcionar, carregando os dados da venda
 * antes de mostrar a página ao usuário.
 * </p>
 */
@Controller
public class PagamentoController implements Serializable {

	private final LojaVirualApplication lojaVirualApplication;

	private final ServiceAsaasBoleto serviceAsaasBoleto;

	private final BoletoJunoRepository boletoJunoRepository;

    private final AsaasConfig asaasConfig;

    public PagamentoController(
            LojaVirualApplication lojaVirualApplication,
            ServiceAsaasBoleto serviceAsaasBoleto,
            BoletoJunoRepository boletoJunoRepository,
            AsaasConfig asaasConfig) {

        this.lojaVirualApplication = lojaVirualApplication;
        this.serviceAsaasBoleto = serviceAsaasBoleto;
        this.boletoJunoRepository = boletoJunoRepository;
        this.asaasConfig = asaasConfig;
    }




	/**
	 * Identificador de versão da serialização.
	 *
	 * <p>
	 * Essa constante existe porque a classe implementa Serializable.
	 * </p>
	 *
	 * <p>
	 * No dia a dia do Spring MVC, você quase nunca mexe nisso manualmente, mas ela
	 * ajuda o Java a controlar versões da classe quando ela precisa ser
	 * serializada.
	 * </p>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Repositório responsável por consultar a venda no banco de dados.
	 *
	 * <p>
	 * Esse objeto é injetado pelo Spring automaticamente com @Autowired.
	 * </p>
	 *
	 * <p>
	 * É ele que faz a busca da venda pelo id.
	 * </p>
	 *
	 * <p>
	 * Em linguagem simples:
	 * </p>
	 * <p>
	 * essa linha conecta o controller com a camada que consulta os dados da venda
	 * no banco.
	 * </p>
	 */
	@Autowired
	private Vd_cp_Loja_virtual_Repository vd_Cp_Loja_virt_repository;

	/**
	 * Serviço responsável por converter ou preparar os dados da venda para envio à
	 * tela.
	 *
	 * <p>
	 * Neste caso, ele é usado para transformar a entidade
	 * {@link VendaCompraLojaVirtual} em {@link VendaCompraLojaVirtualDTO}.
	 * </p>
	 *
	 * <p>
	 * Em linguagem simples:
	 * </p>
	 * <p>
	 * o repository busca a venda bruta do banco, e o service organiza essa venda no
	 * formato que a tela precisa.
	 * </p>
	 */
	@Autowired
	private VendaService vendaService;



	/**
	 * Método responsável por abrir a página de pagamento.
	 *
	 * <p>
	 * Esse método é chamado quando alguém acessa a URL:
	 * </p>
	 * 
	 * <pre>
	 * /pagamento/{idVendaCompra}
	 * </pre>
	 *
	 * <p>
	 * Fluxo do método:
	 * </p>
	 * <ol>
	 * <li>Cria um ModelAndView apontando para a página "pagamento"</li>
	 * <li>Lê o id da venda vindo da URL</li>
	 * <li>Busca a venda no banco</li>
	 * <li>Se não encontrar, envia um DTO vazio para a tela</li>
	 * <li>Se encontrar, converte a venda para DTO e envia para a tela</li>
	 * </ol>
	 *
	 * @param idVendaCompra id da venda recebido pela URL. Exemplo: se a URL for
	 *                      /pagamento/5, esse parâmetro receberá "5".
	 *
	 * @return {@link ModelAndView} apontando para a página "pagamento" com o objeto
	 *         "venda" disponível para o Thymeleaf usar na tela.
	 *
	 * @throws NumberFormatException pode acontecer se o valor recebido na URL não
	 *                               puder ser convertido para Long.
	 *
	 *                               <p>
	 *                               Exemplo simples:
	 *                               </p>
	 *                               <p>
	 *                               Se o navegador chamar /pagamento/12, esse
	 *                               método tenta buscar a venda 12 e mandar os
	 *                               dados dela para pagamento.html.
	 *                               </p>
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/pagamento/{idVendaCompra}")
	public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

		/*
		 * Cria o objeto ModelAndView.
		 *
		 * "pagamento" é o nome da view. No Spring MVC com Thymeleaf, isso normalmente
		 * aponta para: templates/pagamento.html
		 */
		ModelAndView modelAndView = new ModelAndView("pagamento");

		/*
		 * Converte o id recebido na URL de String para Long e busca a venda no banco.
		 *
		 * Exemplo: "10" -> 10L
		 */
		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_Loja_virt_repository
				.findByIdExclusao(Long.parseLong(idVendaCompra));

		System.out.println("COMPRA LOJA VIRTUAL = " + compraLojaVirtual);
		/*
		 * Se não encontrou a venda no banco: envia um DTO vazio para a tela.
		 *
		 * Isso evita que a página fique sem o atributo "venda".
		 */
		if (compraLojaVirtual == null) {
			modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
		} else {
			/*
			 * Se encontrou a venda: chama o service para converter/preparar os dados e
			 * envia o resultado para a tela com o nome "venda".
			 *
			 * Esse nome "venda" será usado no Thymeleaf, por exemplo: ${venda.id}
			 * ${venda.valorTotal} ${venda.pessoa.nome}
			 */
			modelAndView.addObject("venda", vendaService.consultaVenda(compraLojaVirtual));
		}

		/*
		 * Retorna a view "pagamento" já com os dados prontos.
		 */
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/finalizarCompraCartao")
	public ResponseEntity<String> finalizarCompraCartaoAsaas(@RequestParam("cardNumber") String cardNumber,
			@RequestParam("holderName") String holderName, @RequestParam("securityCode") String securityCode,
			@RequestParam("expirationMonth") String expirationMonth,
			@RequestParam("expirationYear") String expirationYear, @RequestParam("idVendaCampo") Long idVendaCampo,
			@RequestParam("cpf") String cpf, @RequestParam("qtdparcela") Integer qtdparcela,
			@RequestParam("cep") String cep, @RequestParam("rua") String rua, @RequestParam("numero") String numero,
			@RequestParam("estado") String estado, @RequestParam("cidade") String cidade) throws Exception {

		VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.findById(idVendaCampo).orElse(null);

		if (idVendaCampo == null) {

			return new ResponseEntity<String>("Código da venda não existe", HttpStatus.OK);

		}

		String cpfLimpo = cpf.replaceAll("\\.", "").replaceAll("\\-", "");

		if (!ValidadorCPF.validar(cpfLimpo)) {

			return new ResponseEntity<String>("CPF informado é invalido", HttpStatus.OK);
		}

		if (qtdparcela > 12 || qtdparcela <= 0) {

			return new ResponseEntity<String>("Quantidade de parcelas deve ser entre 1 e 12", HttpStatus.OK);
		}

		if (vendaCompraLojaVirtual.getValorTotal().doubleValue() <= 0) {

			return new ResponseEntity<String>("O valor da venda não por ser R$0(zero)", HttpStatus.OK);
		}

		List<BoletoJuno> cobrancas = boletoJunoRepository.cobrancaDaVendaCompra(idVendaCampo);

		for (BoletoJuno boletoJuno : cobrancas) {

			boletoJunoRepository.deleteById(boletoJuno.getId());
			boletoJunoRepository.flush();
		}

		/* INICIO GERANDO COBRANCA POR CARTÃO */

		ObjetoPostCarneJuno carne = new ObjetoPostCarneJuno();

		carne.setPayerCpfCnpj(cpfLimpo);
		carne.setPayerName(holderName);
		carne.setPayerPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());

		CobrancaApiAsaasCartao cobrancaApiAsaasCartao = new CobrancaApiAsaasCartao();

		cobrancaApiAsaasCartao.setCustomer(serviceAsaasBoleto.buscaClientePessoaApiAssas(carne));
		cobrancaApiAsaasCartao.setBillingType(AsaasApiPagamentoStatus.CREDIT_CARD);
		cobrancaApiAsaasCartao.setDescription("Venda nº" + idVendaCampo + " realizada por cartão de crédito");

		if (qtdparcela == 1) {

			cobrancaApiAsaasCartao.setInstallmentValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
		} else {

			BigDecimal valorParcela = vendaCompraLojaVirtual.getValorTotal()
					.divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);

			cobrancaApiAsaasCartao.setInstallmentValue(valorParcela.floatValue());
		}

		cobrancaApiAsaasCartao.setInstallmentCount(qtdparcela);
		cobrancaApiAsaasCartao.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

		/* DADOS CARTAO DE CREDITO */

		CartaoCreditoApiAsaas creditCard = new CartaoCreditoApiAsaas();

		creditCard.setCcv(securityCode);
		creditCard.setExpiryMonth(expirationMonth);
		creditCard.setExpiryYear(expirationYear);
		creditCard.setHolderName(holderName);
		creditCard.setNumber(cardNumber);

		cobrancaApiAsaasCartao.setCreditCard(creditCard);

		PessoaFisica pessoaFisica = vendaCompraLojaVirtual.getPessoa();
		CartaoCreditoApiAsaasHolderInfo creditCardHolderInfo = new CartaoCreditoApiAsaasHolderInfo();

		creditCardHolderInfo.setName(pessoaFisica.getNome());
		creditCardHolderInfo.setEmail(pessoaFisica.getEmail());
		creditCardHolderInfo.setCpfCnpj(pessoaFisica.getCpf());
		creditCardHolderInfo.setAddressNumber(numero);
		creditCardHolderInfo.setAddressComplement(null);
		creditCardHolderInfo.setPhone(pessoaFisica.getTelefone());
		creditCardHolderInfo.setMobilePhone(pessoaFisica.getTelefone());
		creditCardHolderInfo.setPostalCode(cep);

		cobrancaApiAsaasCartao.setCreditCardHolderInfo(creditCardHolderInfo);

		String json = new ObjectMapper().writeValueAsString(cobrancaApiAsaasCartao);

		Client client = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoreClient();

		WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments");

		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox()).post(ClientResponse.class, json);

		String StringRetorno = clientResponse.getEntity(String.class);

		int status = clientResponse.getStatus();

		clientResponse.close();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		if (status != 200) {

			for (BoletoJuno boletoJuno : cobrancas) {

				if (boletoJunoRepository.existsById(boletoJuno.getId())) {
					boletoJunoRepository.deleteById(boletoJuno.getId());
					boletoJunoRepository.flush();
				}
			}

			ErroResponseApiAsaasCartaoCredito erroResponseApiAsaasCartaoCredito = objectMapper.readValue(StringRetorno,
					new TypeReference<ErroResponseApiAsaasCartaoCredito>() {
					});

			return new ResponseEntity<String>(
					"Erro ao efetuar cobranca: " + erroResponseApiAsaasCartaoCredito.listErros(), HttpStatus.OK);

		}

		ResponseApiAsaasCartaoCreditoDTO cartaoCredito = objectMapper.readValue(StringRetorno,
				new TypeReference<ResponseApiAsaasCartaoCreditoDTO>() {
				});

		/*
		 * Controla o número da parcela/recorrência. Exemplo: 1 = primeira parcela 2 =
		 * segunda parcela 3 = terceira parcela
		 */
		int recorrencia = 1;

		/*
		 * Lista que vai armazenar todos os registros internos de cobrança que serão
		 * criados para a venda antes de salvar no banco.
		 * 
		 * Mesmo com o nome BoletoJuno, aqui estamos usando essa entidade como controle
		 * interno das parcelas da cobrança da venda.
		 */
		List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();

		/*
		 * Define o formato padrão de data usado no trecho. Exemplo de formato:
		 * 2026-04-20
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		/*
		 * Converte a data de vencimento inicial da cobrança, que veio da API, de texto
		 * (String) para objeto Date.
		 * 
		 * Essa será a data da primeira parcela.
		 */
		Date dataCobranca = dateFormat.parse(cobrancaApiAsaasCartao.getDueDate());

		/*
		 * Objeto auxiliar para manipular datas. Aqui ele será usado para avançar 1 mês
		 * a cada nova parcela.
		 */
		Calendar calendar = Calendar.getInstance();

		for (int p = 1; p <= qtdparcela; p++) {

			/*
			 * Cria um novo objeto de controle interno da cobrança/parcela. Cada volta do
			 * for cria uma parcela diferente.
			 */
			BoletoJuno boletoJuno = new BoletoJuno();

			/*
			 * Salva o id da cobrança do cartão retornado pela API. Isso ajuda a relacionar
			 * a parcela interna com a cobrança real externa.
			 */
			boletoJuno.setChargeICartao(cartaoCredito.getId());

			/*
			 * Salva o link de checkout retornado pela API. Esse link pode ser usado depois
			 * para exibir ou consultar a cobrança.
			 */
			boletoJuno.setCheckoutUrl(cartaoCredito.getInvoiceUrl());

			/*
			 * Salva o código/id principal da cobrança retornada pela API.
			 */
			boletoJuno.setCode(cartaoCredito.getId());

			/*
			 * Define a data de vencimento da parcela atual. Na primeira volta será a data
			 * inicial da cobrança. Nas próximas voltas será a data já avançada em 1 mês.
			 */
			boletoJuno.setDataVencimento(dateFormat.format(dataCobranca));

			/*
			 * Prepara a data da próxima parcela.
			 * 
			 * Fluxo: 1) pega a data atual da parcela 2) soma 1 mês 3) guarda o resultado em
			 * dataCobranca
			 * 
			 * Assim, a próxima volta do for já terá o vencimento do próximo mês.
			 */
			calendar.setTime(dataCobranca);
			calendar.add(Calendar.MONTH, 1);
			dataCobranca = calendar.getTime();

			/*
			 * Relaciona a parcela com a empresa dona da venda.
			 */
			boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());

			/*
			 * Salva o id da cobrança para controle interno e futuras consultas. O nome do
			 * atributo ficou antigo, mas aqui ele está sendo usado mesmo para cobrança de
			 * cartão.
			 */
			boletoJuno.setIdChrBoleto(cartaoCredito.getId());

			/*
			 * Salva o id no campo de Pix também. Isso mostra que a entidade BoletoJuno foi
			 * reaproveitada como controle genérico de cobrança da venda.
			 */
			boletoJuno.setIdPix(cartaoCredito.getId());

			/*
			 * Salva o link da parcela/cobrança.
			 */
			boletoJuno.setInstallmentLink(cartaoCredito.getInvoiceUrl());

			/*
			 * Marca a cobrança como quitada.
			 * 
			 */
			boletoJuno.setQuitado(false);

			/*
			 * Define o número da parcela: 1, 2, 3...
			 */
			boletoJuno.setRecorrencia(recorrencia);

			/*
			 * Define o valor da parcela. Esse valor foi calculado antes, de acordo com a
			 * quantidade de parcelas.
			 */
			boletoJuno.setValor(BigDecimal.valueOf(cobrancaApiAsaasCartao.getInstallmentValue()));

			/*
			 * Relaciona a parcela com a venda.
			 */
			boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

			/*
			 * Adiciona a parcela criada na lista. No final do processo, essa lista será
			 * salva no banco.
			 */
			boletoJunos.add(boletoJuno);

			/*
			 * Avança o número da próxima parcela. Exemplo: se a atual foi 1, a próxima será
			 * 2.
			 */
			recorrencia++;
		}

		boletoJunoRepository.saveAllAndFlush(boletoJunos);

		if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {

			for (BoletoJuno boletoJuno2 : boletoJunos) {
				boletoJunoRepository.quitarBoletoById(boletoJuno2.getId());
			}

			vd_Cp_Loja_virt_repository.updateFinalizaVenda(vendaCompraLojaVirtual.getId());

			return new ResponseEntity<String>("Sucesso", HttpStatus.OK);

		} else {

			return new ResponseEntity<String>("Pagamento não pode ser realizado! Status: " + cartaoCredito.getStatus(),
					HttpStatus.OK);

		}

	}

}
