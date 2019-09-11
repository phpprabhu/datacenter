
package com.ar.businesscard.models.history;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessMessageBulk {

    @SerializedName("messages")
    @Expose
    private List<Object> messages = null;
    @SerializedName("pewCode")
    @Expose
    private Object pewCode;
    @SerializedName("globalIndicator")
    @Expose
    private Object globalIndicator;
    @SerializedName("text")
    @Expose
    private Object text;

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    public Object getPewCode() {
        return pewCode;
    }

    public void setPewCode(Object pewCode) {
        this.pewCode = pewCode;
    }

    public Object getGlobalIndicator() {
        return globalIndicator;
    }

    public void setGlobalIndicator(Object globalIndicator) {
        this.globalIndicator = globalIndicator;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

}
