package org.tradehub.adapter.lowlatency;

import org.tradehub.domain.message.TradehubMessage;

import java.util.HashMap;

public class MutableTradehubMessage extends TradehubMessage {

    public MutableTradehubMessage(){
        super(null, new HashMap<>());
    }

    public void mutateVisitor(final Mutator mutator){
        mutator.mutate(this);
    }

    public void setType(final String type){
        super.type = type;
    }
}
