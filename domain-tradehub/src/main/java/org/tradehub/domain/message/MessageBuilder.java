package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class MessageBuilder {

    @JsonProperty("type")
    String type;
    @JsonProperty("value")
    Payload value;

    public MessageBuilder withPayload(final Payload value){
        this.type = value.getMessageType().value;
        this.value = value;
        return this;
    }

    public Message build(){
        return new Message(this);
    }
}
