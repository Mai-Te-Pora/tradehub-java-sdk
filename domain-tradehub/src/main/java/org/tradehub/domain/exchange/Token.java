package org.tradehub.domain.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(builder = TokenBuilder.class)
public class Token {

    private final String name;
    private final String symbol;
    private final String denom;
    private final Integer decimals;
    private final String blockchain;
    private final Integer chainId;
    private final String assetId;
    private final Boolean isActive;
    private final Boolean isCollateral;
    private final String lockproxyHash;
    private final BigDecimal delegatedSupply;
    private final String originator;

    public Token(final TokenBuilder builder) {
        name = builder.name;
        symbol = builder.symbol;
        denom = builder.denom;
        decimals = builder.decimals;
        blockchain = builder.blockchain;
        chainId = builder.chainId;
        assetId = builder.assetId;
        isActive = builder.isActive;
        isCollateral = builder.isCollateral;
        lockproxyHash = builder.lockproxyHash;
        delegatedSupply = builder.delegatedSupply;
        originator = builder.originator;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDenom() {
        return denom;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public Integer getChainId() {
        return chainId;
    }

    public String getAssetId() {
        return assetId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsCollateral() {
        return isCollateral;
    }

    public String getLockproxyHash() {
        return lockproxyHash;
    }

    public BigDecimal getDelegatedSupply() {
        return delegatedSupply;
    }

    public String getOriginator() {
        return originator;
    }

}
