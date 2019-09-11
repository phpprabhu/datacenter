
package com.ar.businesscard.models.history;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movement {

    @SerializedName("movementText")
    @Expose
    private String movementText;
    @SerializedName("sequenceNb")
    @Expose
    private String sequenceNb;
    @SerializedName("pending")
    @Expose
    private Boolean pending;
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("annexes")
    @Expose
    private List<Object> annexes = null;
    @SerializedName("otherParty")
    @Expose
    private String otherParty;
    @SerializedName("operationId")
    @Expose
    private String operationId;
    @SerializedName("executionDate")
    @Expose
    private String executionDate;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("description1")
    @Expose
    private String description1;
    @SerializedName("description2")
    @Expose
    private String description2;
    @SerializedName("valueDate")
    @Expose
    private String valueDate;
    @SerializedName("detailPresent")
    @Expose
    private Boolean detailPresent;

    public String getMovementText() {
        return movementText;
    }

    public void setMovementText(String movementText) {
        this.movementText = movementText;
    }

    public String getSequenceNb() {
        return sequenceNb;
    }

    public void setSequenceNb(String sequenceNb) {
        this.sequenceNb = sequenceNb;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Object> getAnnexes() {
        return annexes;
    }

    public void setAnnexes(List<Object> annexes) {
        this.annexes = annexes;
    }

    public String getOtherParty() {
        return otherParty;
    }

    public void setOtherParty(String otherParty) {
        this.otherParty = otherParty;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public Boolean getDetailPresent() {
        return detailPresent;
    }

    public void setDetailPresent(Boolean detailPresent) {
        this.detailPresent = detailPresent;
    }

}
