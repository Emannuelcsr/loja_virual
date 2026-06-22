package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class AsaasChargebackCreditCardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String number;
    private String brand;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}