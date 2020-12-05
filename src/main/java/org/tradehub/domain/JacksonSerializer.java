package org.tradehub.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializer {

    private final ObjectMapper mapper;

    public JacksonSerializer() {
        this.mapper = new ObjectMapper();
    }

    public JacksonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T getObjectFromString(Class<T> objClass, String jsonString) throws JsonProcessingException {
        return mapper.readValue(jsonString, objClass);
    }

    public String getStringFromRequestObject(Object msg) throws JsonProcessingException {
        return mapper.writer().writeValueAsString(msg);
    }

    public byte[] getBytesFromRequestObject(Object msg) throws JsonProcessingException {
        return mapper.writer().writeValueAsBytes(msg);
    }
}
