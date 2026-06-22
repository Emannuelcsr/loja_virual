package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class AttachmentNewsLetter implements Serializable {
	private static final long serialVersionUID = 1L;
    private String fileName;
    private String content;
    private String mimeType;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
    
    
}
