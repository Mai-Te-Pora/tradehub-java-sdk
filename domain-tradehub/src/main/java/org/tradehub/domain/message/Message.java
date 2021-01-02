package org.tradehub.domain.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = MessageBuilder.class)
public class Message {

    private String type;
    private Payload value;

    public Message(final MessageBuilder builder) {
        this.type = builder.type;
        this.value = builder.value;
    }

    public String getType() {
        return type;
    }

    public Payload getValue() {
        return value;
    }

    protected void setType(String type) {
        this.type = type;
    }
    protected void setValue(Payload value) {
        this.value = value;
    }
}
