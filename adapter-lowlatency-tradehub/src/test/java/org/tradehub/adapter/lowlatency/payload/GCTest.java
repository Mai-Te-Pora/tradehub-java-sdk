package org.tradehub.adapter.lowlatency.payload;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tradehub.adapter.lowlatency.MutableTradehubMessage;
import org.tradehub.domain.message.payload.CreateOrderBuilder;
import org.tradehub.domain.message.payload.Side;

import java.math.BigDecimal;

public class GCTest {

    static final MutableTradehubMessage classUnderTest = new MutableTradehubMessage();
    static final CreateOrderMutator mutator = new CreateOrderMutator();
    final int rounds = 50000000;
    final String org = "swth1z2jz4uhz8zgt4lq9mq5slz3ukyp3grhl7nsr4x";
    final String market = "btc_z29";

    @Test
    @Disabled
    void testGCRam() {
        double price = 1.0;
        // Mutable
        mutator.withPrice(1.00);
        classUnderTest.mutateVisitor(mutator);
        for (int i = 0; i < rounds; i++) {
            price += 0.1;
            mutator.withPrice(price)
                    .withSide(Side.BUY)
                    .withQuantity(BigDecimal.ONE)
                    .withOriginator(org)
                    .withMarket(market);
            classUnderTest.mutateVisitor(mutator);
        }

        // Immutable
        price = 1.0;
        for (int i = 0; i < rounds; i++) {
            price += 0.1;
            new CreateOrderBuilder()
                    .withPrice(price)
                    .withSide(Side.BUY)
                    .withQuantity(BigDecimal.ONE)
                    .withOriginator(org)
                    .withMarket(market)
                    .build();
        }
    }
}
