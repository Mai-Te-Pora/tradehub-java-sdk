package org.tradehub.domain.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;

@JsonPOJOBuilder
public class TokenBuilder {

    @JsonProperty("name")
    String name;
    @JsonProperty("symbol")
    String symbol;
    @JsonProperty("denom")
    String denom;
    @JsonProperty("decimals")
    Integer decimals;
    @JsonProperty("blockchain")
    String blockchain;
    @JsonProperty("chain_id")
    Integer chainId;
    @JsonProperty("asset_id")
    String assetId;
    @JsonProperty("is_active")
    Boolean isActive;
    @JsonProperty("is_collateral")
    Boolean isCollateral;
    @JsonProperty("lockproxy_hash")
    String lockproxyHash;
    @JsonProperty("delegated_supply")
    BigDecimal delegatedSupply;
    @JsonProperty("originator")
    String originator;

    public TokenBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TokenBuilder withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public TokenBuilder withDenom(String denom) {
        this.denom = denom;
        return this;
    }

    public TokenBuilder withDecimals(Integer decimals) {
        this.decimals = decimals;
        return this;
    }

    public TokenBuilder withBlockchain(String blockchain) {
        this.blockchain = blockchain;
        return this;
    }

    public TokenBuilder withChainId(Integer chainId) {
        this.chainId = chainId;
        return this;
    }

    public TokenBuilder withAssetId(String assetId) {
        this.assetId = assetId;
        return this;
    }

    public TokenBuilder withIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public TokenBuilder withIsColleteral(Boolean isCollateral) {
        this.isCollateral = isCollateral;
        return this;
    }

    public TokenBuilder withLockproxyHash(String lockproxyHash) {
        this.lockproxyHash = lockproxyHash;
        return this;
    }

    public TokenBuilder withDelegatedSupply(BigDecimal delegatedSupply) {
        this.delegatedSupply = delegatedSupply;
        return this;
    }

    public TokenBuilder withOriginator(String originator) {
        this.originator = originator;
        return this;
    }

    public Token build() {
        return new Token(this);
    }
}
