package org.tradehub.adapter.lowlatency.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.tradehub.adapter.lowlatency.MutableTradehubMessage;
import org.tradehub.domain.message.payload.Side;
import org.tradehub.domain.util.JacksonSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreateOrderMutatorTest {

    private final MutableTradehubMessage classUnderTest = new MutableTradehubMessage();
    private final CreateOrderMutator mutator = new CreateOrderMutator();
    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Test
    void serialize_when_mutableCreateOrderObjectMutated_then_sameObjectMutates() throws JsonProcessingException {

        // act
        classUnderTest.mutateVisitor(mutator);
        final String initialState = jacksonSerializer.getStringFromRequestObject(classUnderTest);
        mutator.withPrice(2.02)
               .withSide(Side.BUY);
        classUnderTest.mutateVisitor(mutator);
        final String mutatedState = jacksonSerializer.getStringFromRequestObject(classUnderTest);
        mutator.clearState();
        classUnderTest.mutateVisitor(mutator);
        final String backToInitialState = jacksonSerializer.getStringFromRequestObject(classUnderTest);

        // assert
        assertEquals(initialState, backToInitialState);
        assertNotEquals(initialState, mutatedState);
    }

}
