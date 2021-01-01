package org.tradehub.domain.message.payload;

public enum OrderTimeInForce {

    GTC("gtc"),
    FOK("fok"),
    IOC("ioc");

    public final String value;

    OrderTimeInForce(String value) {
        this.value = value;
    }
}
