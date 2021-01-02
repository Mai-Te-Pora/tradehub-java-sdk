package org.tradehub.domain.message.payload;


import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.tradehub.domain.message.TradehubMessage;
import org.tradehub.domain.util.JacksonSerializer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestWithResources
public class CreateOrderTest {

    @GivenTextResource("payloads/create_order.json")
    static String createOrderString;

    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Test
    void deserialize_when_createOrderJson_then_createOrderObject() throws JsonProcessingException {
        // act
        final TradehubMessage createOrder = jacksonSerializer.getObjectFromString(TradehubMessage.class, createOrderString);

        // assert
        assertNotNull(createOrder);
    }

    @Test
    void serialize_when_createOrderMessage_then_createOrderJson() throws JsonProcessingException {
        // arrange
        final TradehubMessage createOrder = new CreateOrderBuilder()
                .withMarket("swth_eth")
                .withSide(Side.SELL)
                .withQuantity(new BigDecimal(200))
                .withPrice(1.01)
                .withOriginator("swth1z2jz4uhz8zgt4lq9mq5slz3ukyp3grhl7nsr4x")
                .build();

        // act
        final String result = jacksonSerializer.getStringFromRequestObject(createOrder);

        // assert
        assertEquals(createOrderString, result);
    }

}
