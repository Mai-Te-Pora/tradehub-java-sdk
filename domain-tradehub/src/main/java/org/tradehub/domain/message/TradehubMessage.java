package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TradehubMessage {

    @JsonProperty("type")
    protected String type;
    @JsonProperty("value")
    protected Map<String, String> value;

    @JsonCreator
    protected TradehubMessage(@JsonProperty("type") String type,
                              @JsonProperty("value") Map<String, String> value){
        this.type = type;
        this.value = value;
    }

    @JsonIgnore
    protected TradehubMessage(){}

    @JsonIgnore
    public TradehubMessage(final TradehubMessageBuilder builder) {
        this.type = builder.getType();
        this.value = builder.getValue();
        builder.verify();
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getValue() {
        return value;
    }
}
