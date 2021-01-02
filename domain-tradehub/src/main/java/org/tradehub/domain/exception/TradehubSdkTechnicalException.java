package org.tradehub.domain.exception;

public class TradehubSdkTechnicalException extends RuntimeException {

    public TradehubSdkTechnicalException(String message) {
        super(message);
    }

    public TradehubSdkTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
