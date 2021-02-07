package org.tradehub.domain.message;

import java.util.Map;

public interface TradehubMessageBuilder {

    String getType();
    Map<String, String> getValue();
    void verify();
    TradehubMessage build();
}
