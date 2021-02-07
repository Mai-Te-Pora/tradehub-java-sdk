package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPOJOBuilder
public class FeeBuilder {

    @JsonProperty("amount")
    List<Map<String, String>> amount = new ArrayList<>();
    @JsonProperty("gas")
    String gas;

    public FeeBuilder withAmount(final String denom, long amount){
        final Map<String, String> amountPair = new HashMap<>();
        amountPair.put(denom, String.valueOf(amount));
        this.amount.add(amountPair);
        return this;
    }

    public FeeBuilder withGas(final long gas){
        this.gas = String.valueOf(gas);
        return this;
    }

    public Fee build(){
        return new Fee(this);
    }
}
