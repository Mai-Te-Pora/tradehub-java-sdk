package org.tradehub.domain.exchange;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.tradehub.domain.util.JacksonSerializer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestWithResources
class TokenTest {

    @GivenTextResource("payloads/token.json")
    static String tokenString;

    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @Test
    void deserialize_when_jsonTokenObject_then_jsonDomainObject() throws JsonProcessingException {
        // act
        final Token result = jacksonSerializer.getObjectFromString(Token.class, tokenString);

        // assert
        assertEquals("Switcheo", result.getName());
        assertEquals("swth", result.getSymbol());
        assertEquals("swth", result.getDenom());
        assertEquals(8, result.getDecimals());
        assertEquals("neo", result.getBlockchain());
        assertEquals(4, result.getChainId());
        assertEquals("27ed90527afd253ab30a4f207a0882c8567a93c9", result.getAssetId());
        assertEquals(true, result.getIsActive());
        assertEquals(false, result.getIsCollateral());
        assertEquals("", result.getLockproxyHash());
        assertEquals(BigDecimal.valueOf(100000000000000000L), result.getDelegatedSupply());
        assertEquals("tswth1mw90en8tcqnvdjhp64qmyhuq4qasvhy2s6st4t", result.getOriginator());
    }

    @Test
    void build_when_objectGetsCreatedWithBuilder_then_ObjectConstructed() throws JsonProcessingException {

        // act
        final Token result = new TokenBuilder()
                .withName("Switcheo")
                .withSymbol("swth")
                .withDenom("denom")
                .withDecimals(8)
                .withBlockchain("neo")
                .withChainId(4)
                .withAssetId("27ed90527afd253ab30a4f207a0882c8567a93c9")
                .withIsActive(true)
                .withIsColleteral(false)
                .withLockproxyHash("")
                .withDelegatedSupply(BigDecimal.valueOf(100000000000000000L))
                .withOriginator("tswth1mw90en8tcqnvdjhp64qmyhuq4qasvhy2s6st4t")
                .build();

        // assert
        assertNotNull(result);
    }

}