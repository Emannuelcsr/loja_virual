package jdev.mentoria.lojavirtual;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AsaasConfig {
	

    @Value("${asaas.api.key-sandbox}")
    private String apiKeySandbox;

    public String getApiKeySandbox() {
        return apiKeySandbox;
    }

}
