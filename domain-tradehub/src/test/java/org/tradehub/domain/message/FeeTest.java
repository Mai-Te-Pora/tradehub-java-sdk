package org.tradehub.domain.message;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.tradehub.domain.util.JacksonSerializer;

import static org.junit.jupiter.api.Assertions.*;

@TestWithResources
class FeeTest {

    @GivenTextResource("payloads/fee.json")
    static String feeString;

    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Test
    void deserialize_when_jsonFeeObject_then_jsonDomainObject() throws JsonProcessingException {

        final Fee result = jacksonSerializer.getObjectFromString(Fee.class, feeString);
        final Fee reference = new FeeBuilder()
                .withAmount("swth", 100000000L)
                .withGas(100000000000L)
                .build();

        assertEquals(reference.getGas(), result.getGas());
        assertEquals(1, result.getAmount().size());
        assertEquals(result.getAmount().get(0).get("swth"), result.getAmount().get(0).get("swth"));
    }
}