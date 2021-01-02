package org.tradehub.domain.exception;

public class TradehubSdkBusinessException extends Exception {

    public TradehubSdkBusinessException(String message) {
        super(message);
    }

    public TradehubSdkBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
