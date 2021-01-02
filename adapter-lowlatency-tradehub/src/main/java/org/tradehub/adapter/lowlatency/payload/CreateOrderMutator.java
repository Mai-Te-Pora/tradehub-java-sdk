package org.tradehub.adapter.lowlatency.payload;

import org.tradehub.adapter.lowlatency.MutableTradehubMessage;
import org.tradehub.adapter.lowlatency.Mutator;
import org.tradehub.domain.message.payload.CreateOrderBuilder;
import org.tradehub.domain.message.payload.OrderTimeInForce;
import org.tradehub.domain.message.payload.OrderType;
import org.tradehub.domain.message.payload.Side;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CreateOrderMutator implements Mutator {

    private final Map<String, String> value = new HashMap<>();

    public CreateOrderMutator(){
        setInitialState();
    }

    @Override
    public void mutate(final MutableTradehubMessage mutableTradehubMessage) {
        mutableTradehubMessage.setType(CreateOrderBuilder.TYPE);
        mutableTradehubMessage.getValue().clear();
        for(String key : value.keySet()){
            mutableTradehubMessage.getValue().put(key, value.get(key));
        }
    }

    @Override
    public void clearState() {
        value.clear();
        setInitialState();
    }

    private void setInitialState(){
        value.put(CreateOrderBuilder.KEY_MARKET, OrderType.LIMIT.value);
        value.put(CreateOrderBuilder.KEY_ORDER_TIME_IN_FORCE, OrderTimeInForce.GTC.value);
        value.put(CreateOrderBuilder.KEY_IS_POST_ONLY, "false");
        value.put(CreateOrderBuilder.KEY_IS_REDUCE_ONLY, "false");
    }

    public CreateOrderMutator withMarket(final String market){
        value.put(CreateOrderBuilder.KEY_MARKET, market);
        return this;
    }

    public CreateOrderMutator withSide(final Side side){
        value.put(CreateOrderBuilder.KEY_SIDE, side.value);
        return this;
    }

    public CreateOrderMutator withQuantity(final BigDecimal quantity){
        value.put(CreateOrderBuilder.KEY_QUANTITY, quantity.toPlainString());
        return this;
    }

    public CreateOrderMutator withOrderType(final OrderType orderType){
        value.put(CreateOrderBuilder.KEY_ORDER_TYPE, orderType.value);
        return this;
    }

    public CreateOrderMutator withPrice(final double price){
        value.put(CreateOrderBuilder.KEY_PRICE, String.valueOf(price));
        return this;
    }

    public CreateOrderMutator withStopPrice(final double stopPrice){
        value.put(CreateOrderBuilder.KEY_STOP_PRICE, String.valueOf(stopPrice));
        return this;
    }

    public CreateOrderMutator withOrderTimeInForce(final OrderTimeInForce timeInForce){
        value.put(CreateOrderBuilder.KEY_ORDER_TIME_IN_FORCE, timeInForce.value);
        return this;
    }

    public CreateOrderMutator withTriggerType(final String triggerType){
        value.put(CreateOrderBuilder.KEY_TRIGGER_TYPE, triggerType);
        return this;
    }

    public CreateOrderMutator withIsPostOnly(final boolean isPostOnly){
        value.put(CreateOrderBuilder.KEY_IS_POST_ONLY, String.valueOf(isPostOnly));
        return this;
    }

    public CreateOrderMutator withIsReduceOnly(final boolean isReduceOnly){
        value.put(CreateOrderBuilder.KEY_IS_REDUCE_ONLY, String.valueOf(isReduceOnly));
        return this;
    }

    public CreateOrderMutator withOriginator(final String originator){
        value.put(CreateOrderBuilder.KEY_ORIGINATOR, originator);
        return this;
    }
}
