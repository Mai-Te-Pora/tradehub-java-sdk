package org.tradehub.domain.message.payload;

public enum OrderType {

    LIMIT("limit"),
    MARKET("market"),
    STOP_LIMIT("stop-limit"),
    STOP_MARKET("stop-market");

    public final String value;

    OrderType(String value) {
        this.value = value;
    }
}
