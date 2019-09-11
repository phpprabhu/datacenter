
package com.ar.businesscard.models.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountHistoryResponse {

    @SerializedName("value")
    @Expose
    private Value value;
    @SerializedName("businessMessageBulk")
    @Expose
    private BusinessMessageBulk businessMessageBulk;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public BusinessMessageBulk getBusinessMessageBulk() {
        return businessMessageBulk;
    }

    public void setBusinessMessageBulk(BusinessMessageBulk businessMessageBulk) {
        this.businessMessageBulk = businessMessageBulk;
    }

}
