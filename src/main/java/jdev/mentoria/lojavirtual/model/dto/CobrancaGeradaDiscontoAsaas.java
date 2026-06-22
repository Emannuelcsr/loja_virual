package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CobrancaGeradaDiscontoAsaas implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal value;
    private Integer dueDateLimitDays;
    private String type;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getDueDateLimitDays() {
        return dueDateLimitDays;
    }

    public void setDueDateLimitDays(Integer dueDateLimitDays) {
        this.dueDateLimitDays = dueDateLimitDays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}