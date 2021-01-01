package org.tradehub.domain.message.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.tradehub.domain.message.MessageType;
import org.tradehub.domain.message.Payload;

import static java.util.Objects.requireNonNull;

@JsonDeserialize(builder = CreateOrderBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrder implements Payload {

    private final String market;
    private final String side;
    private final String quantity;
    private final String type;
    private final String price;
    private final String stopPrice;
    private final String timeInForce;
    private final String triggerType;
    private final boolean isReduceOnly;
    private final boolean isPostOnly;
    private final String originator;

    public CreateOrder(final CreateOrderBuilder builder){
        this.market = requireNonNull(builder.market, "Market field must be non null");
        this.side = requireNonNull(builder.side, "Side field must be non null");
        this.quantity = requireNonNull(builder.quantity, "Quantity field must be non null");
        this.type = requireNonNull(builder.type, "Type field must be non null");
        this.originator = requireNonNull(builder.originator, "Originator field must be not null");
        this.price = builder.type.equals(OrderType.LIMIT.value) || builder.type.equals(OrderType.STOP_LIMIT.value)
                ? requireNonNull(builder.price, "The price field must be non null on limit orders")
                : builder.price;
        this.stopPrice = builder.type.equals(OrderType.STOP_LIMIT.value)
                ? requireNonNull(builder.stopPrice, "The stop price must be non null on stop-limit orders")
                : builder.stopPrice;
        this.timeInForce = builder.timeInForce;
        this.triggerType = builder.type.equals(OrderType.STOP_MARKET.value) || builder.type.equals(OrderType.STOP_LIMIT.value)
                ? requireNonNull(builder.triggerType, "The trigger type field must be non null on stop-limit/stop-market orders")
                : builder.triggerType;
        this.isReduceOnly = builder.isReduceOnly;
        this.isPostOnly = builder.isPostOnly;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CREATE_ORDER_MSG_TYPE;
    }

    public String getMarket() {
        return market;
    }

    public String getSide() {
        return side;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public Boolean getReduceOnly() {
        return isReduceOnly;
    }

    public Boolean getPostOnly() {
        return isPostOnly;
    }

    public String getOriginator() {
        return originator;
    }
}
