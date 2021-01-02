package org.tradehub.adapter.lowlatency;

public interface Mutator {

    void mutate(MutableTradehubMessage tradehubMessage);
    void clearState();
}
