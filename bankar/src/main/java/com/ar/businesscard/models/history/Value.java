
package com.ar.businesscard.models.history;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("numberPending")
    @Expose
    private Integer numberPending;
    @SerializedName("completeListFlag")
    @Expose
    private Boolean completeListFlag;
    @SerializedName("movements")
    @Expose
    private List<Movement> movements = null;

    public Integer getNumberPending() {
        return numberPending;
    }

    public void setNumberPending(Integer numberPending) {
        this.numberPending = numberPending;
    }

    public Boolean getCompleteListFlag() {
        return completeListFlag;
    }

    public void setCompleteListFlag(Boolean completeListFlag) {
        this.completeListFlag = completeListFlag;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

}
