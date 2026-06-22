package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class CobrancaGeradaCreditCardAsaas implements Serializable {

    private static final long serialVersionUID = 1L;

    private String creditCardNumber;
    private String creditCardBrand;
    private String creditCardToken;

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardBrand() {
        return creditCardBrand;
    }

    public void setCreditCardBrand(String creditCardBrand) {
        this.creditCardBrand = creditCardBrand;
    }

    public String getCreditCardToken() {
        return creditCardToken;
    }

    public void setCreditCardToken(String creditCardToken) {
        this.creditCardToken = creditCardToken;
    }
}