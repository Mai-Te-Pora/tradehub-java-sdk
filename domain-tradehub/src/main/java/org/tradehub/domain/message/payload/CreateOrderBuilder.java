package org.tradehub.domain.message.payload;

import org.tradehub.domain.exception.TradehubSdkTechnicalException;
import org.tradehub.domain.message.MessageType;
import org.tradehub.domain.message.TradehubMessage;
import org.tradehub.domain.message.TradehubMessageBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CreateOrderBuilder implements TradehubMessageBuilder {

    private final Map<String, String> value = new HashMap<>();
    public static final String TYPE = MessageType.CREATE_ORDER_MSG_TYPE.value;
    public static final String KEY_MARKET = "Market";
    public static final String KEY_SIDE = "Side";
    public static final String KEY_QUANTITY = "Quantity";
    public static final String KEY_ORDER_TYPE = "OrderType";
    public static final String KEY_PRICE = "Price";
    public static final String KEY_STOP_PRICE = "StopPrice";
    public static final String KEY_ORDER_TIME_IN_FORCE = "OrderTimeInForce";
    public static final String KEY_TRIGGER_TYPE = "TriggerType";
    public static final String KEY_IS_POST_ONLY = "IsPostOnly";
    public static final String KEY_IS_REDUCE_ONLY = "IsReduceOnly";
    public static final String KEY_ORIGINATOR = "Originator";

    public CreateOrderBuilder() {
        value.put(KEY_ORDER_TYPE, OrderType.LIMIT.value);
        value.put(KEY_ORDER_TIME_IN_FORCE, OrderTimeInForce.GTC.value);
        value.put(KEY_IS_POST_ONLY, "false");
        value.put(KEY_IS_REDUCE_ONLY, "false");
    }
    public CreateOrderBuilder withMarket(final String market){
        value.put(KEY_MARKET, market);
        return this;
    }

    public CreateOrderBuilder withSide(final Side side){
        value.put(KEY_SIDE, side.value);
        return this;
    }

    public CreateOrderBuilder withQuantity(final BigDecimal quantity){
        value.put(KEY_QUANTITY, quantity.toPlainString());
        return this;
    }

    public CreateOrderBuilder withOrderType(final OrderType orderType){
        value.put(KEY_ORDER_TYPE, orderType.value);
        return this;
    }

    public CreateOrderBuilder withPrice(final double price){
        value.put(KEY_PRICE, String.valueOf(price));
        return this;
    }

    public CreateOrderBuilder withStopPrice(final double stopPrice){
        value.put(KEY_STOP_PRICE, String.valueOf(stopPrice));
        return this;
    }

    public CreateOrderBuilder withOrderTimeInForce(final OrderTimeInForce timeInForce){
        value.put(KEY_ORDER_TIME_IN_FORCE, timeInForce.value);
        return this;
    }

    public CreateOrderBuilder withTriggerType(final String triggerType){
        value.put(KEY_TRIGGER_TYPE, triggerType);
        return this;
    }

    public CreateOrderBuilder withIsPostOnly(final boolean isPostOnly){
        value.put(KEY_IS_POST_ONLY, String.valueOf(isPostOnly));
        return this;
    }

    public CreateOrderBuilder withIsReduceOnly(final boolean isReduceOnly){
        value.put(KEY_IS_REDUCE_ONLY, String.valueOf(isReduceOnly));
        return this;
    }

    public CreateOrderBuilder withOriginator(final String originator){
        value.put(KEY_ORIGINATOR, originator);
        return this;
    }

    @Override
    public TradehubMessage build() {
        return new TradehubMessage(this);
    }

    @Override
    public void verify(){
        try {
            requireNonNull(value.get(KEY_MARKET), "Market field must be non null");
            requireNonNull(value.get(KEY_SIDE), "Side field must be non null");
            requireNonNull(value.get(KEY_QUANTITY), "Quantity field must be non null");
            requireNonNull(value.get(KEY_ORDER_TYPE), "OrderType field must be non null");
            requireNonNull(value.get(KEY_ORIGINATOR), "Originator field must be not null");
            if (value.get(KEY_ORDER_TYPE).equals(OrderType.LIMIT.value) || value.get(KEY_ORDER_TYPE).equals(OrderType.STOP_LIMIT.value)) {
                requireNonNull(value.get(KEY_PRICE), "The price field must be non null on limit orders");
            }
            if (value.get(KEY_ORDER_TYPE).equals(OrderType.STOP_LIMIT.value)) {
                requireNonNull(value.get(KEY_STOP_PRICE), "The stop price must be non null on stop-limit orders");
            }
            if (value.get(KEY_ORDER_TYPE).equals(OrderType.STOP_MARKET.value) || value.get(KEY_ORDER_TYPE).equals(OrderType.STOP_LIMIT.value)) {
                requireNonNull(value.get(KEY_TRIGGER_TYPE), "The trigger type field must be non null on stop-limit/stop-market orders");
            }
        } catch (Exception e){
            throw new TradehubSdkTechnicalException("CreateOrder payload is not valid", e);
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Map<String, String> getValue() {
        return this.value;
    }
}
