package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

@JsonDeserialize(builder = FeeBuilder.class)
public class Fee {

    @JsonProperty("amount")
    private final List<Map<String, String>> amount;
    @JsonProperty("gas")
    private final String gas;

    public Fee(final FeeBuilder builder) {
        this.amount = builder.amount;
        this.gas = builder.gas;
    }

    public List<Map<String, String>> getAmount() {
        return amount;
    }

    public String getGas() { return gas; }
}
