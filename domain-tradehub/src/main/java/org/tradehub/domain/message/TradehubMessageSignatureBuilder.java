package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonPOJOBuilder
public class TradehubMessageSignatureBuilder {

    @JsonProperty("accountNumber")
    String accountNumber;
    @JsonProperty("chainId")
    String chainId;
    @JsonProperty("fee")
    Fee fee;
    @JsonProperty("msgs")
    List<TradehubMessage> tradehubMessages;
    @JsonProperty("sequence")
    String sequence;

    public TradehubMessageSignatureBuilder withAccountNumber(final long accountNumber){
        this.accountNumber = String.valueOf(accountNumber);
        return this;
    }

    public TradehubMessageSignatureBuilder withChainId(final String chainId){
        this.chainId = chainId;
        return this;
    }

    public TradehubMessageSignatureBuilder withFee(final Fee fee){
        this.fee = fee;
        return this;
    }

    public TradehubMessageSignatureBuilder withTradehubMessages(final List<TradehubMessage> tradehubMessages){
        this.tradehubMessages = tradehubMessages;
        return this;
    }

    public TradehubMessageSignatureBuilder withSequence(final long sequence){
        this.sequence = String.valueOf(sequence);
        return this;
    }

    public TradehubMessageSignature build(){
        return new TradehubMessageSignature(this);
    }
}
