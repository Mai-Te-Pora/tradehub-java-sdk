package org.tradehub.domain.message;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.tradehub.domain.message.payload.CreateOrderBuilder;
import org.tradehub.domain.message.payload.Side;
import org.tradehub.domain.util.JacksonSerializer;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestWithResources
public class TradehubMessageSignatureTest {

    @GivenTextResource("payloads/signature.json")
    static String signatureJson;

    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Test
    void deserialize_when_jsonFeeObject_then_jsonDomainObject() throws JsonProcessingException {
        // arrange
        final TradehubMessageSignature payload = new TradehubMessageSignatureBuilder()
                .withAccountNumber(5L)
                .withChainId("3")
                .withFee(new FeeBuilder()
                        .withAmount("swth", 100000000L)
                        .withGas(100000000000L)
                        .build())
                .withTradehubMessages(Collections.singletonList(new CreateOrderBuilder()
                        .withMarket("swth_eth")
                        .withSide(Side.SELL)
                        .withQuantity(new BigDecimal(200))
                        .withPrice(1.01)
                        .withOriginator("swth1z2jz4uhz8zgt4lq9mq5slz3ukyp3grhl7nsr4x")
                        .build()))
                .withSequence(1L)
                .build();
        final TradehubMessageSignature result = jacksonSerializer.getObjectFromString(TradehubMessageSignature.class, signatureJson);
        assertNotNull(result);
        assertEquals(payload.getChainId(), result.getChainId());
        assertEquals(payload.getTradehubMessages().size(), result.getTradehubMessages().size());
    }
}
