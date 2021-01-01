package org.tradehub.domain.message.payload;

public enum Side {

    SELL("sell"),
    BUY("buy");

    public final String value;

    Side(String value) {
        this.value = value;
    }
}
