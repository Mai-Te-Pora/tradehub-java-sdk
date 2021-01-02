package org.tradehub.domain.exchange;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(builder = TokenBuilder.class)
public class Token {

    protected String name;
    protected String symbol;
    protected String denom;
    protected int decimals;
    protected String blockchain;
    protected int chainId;
    protected final String assetId;
    protected final boolean isActive;
    protected final boolean isCollateral;
    protected final String lockproxyHash;
    protected final BigDecimal delegatedSupply;
    protected final String originator;

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

    public int getDecimals() {
        return decimals;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public int getChainId() {
        return chainId;
    }

    public String getAssetId() {
        return assetId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public boolean getIsCollateral() {
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
