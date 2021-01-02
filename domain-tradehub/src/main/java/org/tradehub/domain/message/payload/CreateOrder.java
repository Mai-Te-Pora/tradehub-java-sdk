package org.tradehub.domain.message.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.tradehub.domain.message.MessageType;
import org.tradehub.domain.message.Payload;

import static java.util.Objects.requireNonNull;

@JsonDeserialize(builder = CreateOrderBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrder implements Payload {

    protected String market;
    protected String side;
    protected String quantity;
    protected String type;
    protected String price;
    protected String stopPrice;
    protected String timeInForce;
    protected String triggerType;
    protected boolean isReduceOnly;
    protected boolean isPostOnly;
    protected String originator;

    public CreateOrder(final CreateOrderBuilder builder){
        this.market = builder.market;
        this.side = builder.side;
        this.quantity = builder.quantity;
        this.type = builder.type;
        this.originator = builder.originator;
        this.price = builder.price;
        this.stopPrice = builder.stopPrice;
        this.timeInForce = builder.timeInForce;
        this.triggerType = builder.triggerType;
        this.isReduceOnly = builder.isReduceOnly;
        this.isPostOnly = builder.isPostOnly;
        verify();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CREATE_ORDER_MSG_TYPE;
    }

    @Override
    public void verify(){
        requireNonNull(this.market, "Market field must be non null");
        requireNonNull(this.side, "Side field must be non null");
        requireNonNull(this.quantity, "Quantity field must be non null");
        requireNonNull(this.type, "Type field must be non null");
        requireNonNull(this.originator, "Originator field must be not null");
        if(this.type.equals(OrderType.LIMIT.value) || this.type.equals(OrderType.STOP_LIMIT.value)) {
            requireNonNull(this.price, "The price field must be non null on limit orders");
        }
        if(this.type.equals(OrderType.STOP_LIMIT.value)) {
            requireNonNull(this.stopPrice, "The stop price must be non null on stop-limit orders");
        }
        if(this.type.equals(OrderType.STOP_MARKET.value) || this.type.equals(OrderType.STOP_LIMIT.value)){
                requireNonNull(this.triggerType, "The trigger type field must be non null on stop-limit/stop-market orders");
        }
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
