package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * DTO usado para criar uma newsletter/mensagem no GetResponse.
 *
 * Essa classe representa o JSON enviado para o endpoint de criação
 * de newsletter.
 *
 * Campos obrigatórios segundo a API:
 * - subject
 * - fromField
 * - campaign
 * - content
 * - sendSettings
 *
 * A mensagem pode ser criada como:
 * - broadcast: mensagem para envio
 * - draft: rascunho
 */

public class CreateMessageDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	   /**
     * Conteúdo da mensagem.
     *
     * Contém o corpo em HTML e/ou texto puro.
     * Exemplo:
     * - html: "<h1>test 12</h1>"
     * - plain: "test 12 Some test"
     */
	private ContentNewsLetter content = new ContentNewsLetter();;
	
	
    /**
     * Configurações extras da mensagem.
     *
     * Exemplo:
     * - "openrate"
     *
     * Serve para ativar recursos relacionados ao rastreamento
     * ou comportamento da mensagem.
     */
    private List<String> flags = new ArrayList<String>();
    
    /**
     * Nome interno da newsletter.
     *
     * Esse nome serve para organização dentro do GetResponse.
     * Não é necessariamente o assunto que o cliente verá no e-mail.
     *
     * Pela documentação:
     * - mínimo: 2 caracteres
     * - máximo: 128 caracteres
     */
    private String name;
    
    /**
     * Tipo da newsletter.
     *
     * Valores aceitos:
     * - broadcast: mensagem de envio normal
     * - draft: rascunho
     *
     * Valor padrão da API: broadcast.
     */
    private String type = "broadcast";
    
    /**
     * Assunto do e-mail.
     *
     * Esse é o texto que o destinatário verá como título do e-mail
     * na caixa de entrada.
     *
     * Pela documentação:
     * - mínimo: 2 caracteres
     * - máximo: 128 caracteres
     */
    private String subject;
    
    /**
     * Remetente da mensagem.
     *
     * Representa o endereço de e-mail que aparece como "De".
     * A API espera um objeto com fromFieldId.
     */
    private FromFieldNewsLetter fromField = new FromFieldNewsLetter();;
    
    /**
     * Endereço usado para resposta.
     *
     * Quando a pessoa clicar em responder o e-mail,
     * a resposta será enviada para esse remetente.
     */
    private ReplyTo replyTo = new ReplyTo();;
    

    /**
     * Campanha/lista associada à newsletter.
     *
     * No GetResponse, "campaign" representa uma lista de contatos.
     * A newsletter precisa estar vinculada a uma campanha.
     */
    private CampaignNewsLetter campaign = new CampaignNewsLetter();
    
    /**
     * Anexos da newsletter.
     *
     * A API permite anexos, mas a soma total dos arquivos
     * não pode ultrapassar 400KB.
     */
    private List<AttachmentNewsLetter> attachments = new ArrayList<AttachmentNewsLetter>();
    
    /**
     * Configurações de envio da mensagem.
     *
     * Define para quem a mensagem será enviada,
     * quais campanhas, segmentos, contatos específicos,
     * exclusões, agendamento e recursos como Perfect Timing.
     */
    private SendSettingsNewsLetter sendSettings = new SendSettingsNewsLetter();
    
    
    
	public ContentNewsLetter getContent() {
		return content;
	}
	public void setContent(ContentNewsLetter content) {
		this.content = content;
	}
	public List<String> getFlags() {
		return flags;
	}
	public void setFlags(List<String> flags) {
		this.flags = flags;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public FromFieldNewsLetter getFromField() {
		return fromField;
	}
	public void setFromField(FromFieldNewsLetter fromField) {
		this.fromField = fromField;
	}
	public ReplyTo getReplyTo() {
		return replyTo;
	}
	
	
	public void setReplyTo(ReplyTo replyTo) {
		this.replyTo = replyTo;
	}
	public CampaignNewsLetter getCampaign() {
		return campaign;
	}
	public void setCampaign(CampaignNewsLetter campaign) {
		this.campaign = campaign;
	}
	public List<AttachmentNewsLetter> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentNewsLetter> attachments) {
		this.attachments = attachments;
	}
	public SendSettingsNewsLetter getSendSettings() {
		return sendSettings;
	}
	public void setSendSettings(SendSettingsNewsLetter sendSettings) {
		this.sendSettings = sendSettings;
	}
	
    
    
}
