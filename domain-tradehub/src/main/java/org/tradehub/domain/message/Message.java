package org.tradehub.domain.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = MessageBuilder.class)
public class Message {

    private final String type;
    private final Payload value;

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
}
