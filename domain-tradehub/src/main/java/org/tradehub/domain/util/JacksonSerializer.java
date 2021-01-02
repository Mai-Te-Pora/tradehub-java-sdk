package org.tradehub.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonSerializer {

    private final ObjectMapper mapper;

    public JacksonSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        this.mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
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
