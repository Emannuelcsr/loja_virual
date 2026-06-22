package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AsaasRefundDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dateCreated;
    private String status;
    private BigDecimal value;
    private String endToEndIdentifier;
    private String description;
    private String effectiveDate;
    private String transactionReceiptUrl;
    private List<AsaasRefundedSplitDTO> refundedSplits = new ArrayList<AsaasRefundedSplitDTO>();

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getEndToEndIdentifier() {
        return endToEndIdentifier;
    }

    public void setEndToEndIdentifier(String endToEndIdentifier) {
        this.endToEndIdentifier = endToEndIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getTransactionReceiptUrl() {
        return transactionReceiptUrl;
    }

    public void setTransactionReceiptUrl(String transactionReceiptUrl) {
        this.transactionReceiptUrl = transactionReceiptUrl;
    }

    public List<AsaasRefundedSplitDTO> getRefundedSplits() {
        return refundedSplits;
    }

    public void setRefundedSplits(List<AsaasRefundedSplitDTO> refundedSplits) {
        this.refundedSplits = refundedSplits;
    }
}
