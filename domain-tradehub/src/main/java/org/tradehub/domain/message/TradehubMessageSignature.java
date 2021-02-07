package org.tradehub.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(builder = TradehubMessageSignatureBuilder.class)
public class TradehubMessageSignature {

    @JsonProperty("accountNumber")
    private final String accountNumber;
    @JsonProperty("chainId")
    private final String chainId;
    @JsonProperty("fee")
    private final Fee fee;
    @JsonProperty("msgs")
    private final List<TradehubMessage> tradehubMessages;
    @JsonProperty("sequence")
    private final String sequence;

    public TradehubMessageSignature(final TradehubMessageSignatureBuilder builder) {
        this.accountNumber = builder.accountNumber;
        this.chainId = builder.chainId;
        this.fee = builder.fee;
        this.tradehubMessages = builder.tradehubMessages;
        this.sequence = builder.sequence;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getChainId() {
        return chainId;
    }

    public Fee getFee() {
        return fee;
    }

    public List<TradehubMessage> getTradehubMessages() {
        return tradehubMessages;
    }

    public String getSequence() {
        return sequence;
    }
}
