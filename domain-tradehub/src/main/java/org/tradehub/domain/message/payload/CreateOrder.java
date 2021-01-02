package org.tradehub.domain.message.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.tradehub.domain.message.MessageType;
import org.tradehub.domain.message.Payload;

import static java.util.Objects.requireNonNull;

@JsonDeserialize(builder = CreateOrderBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrder implements Payload {

    private String market;
    private String side;
    private String quantity;
    private String type;
    private String price;
    private String stopPrice;
    private String timeInForce;
    private String triggerType;
    private boolean isReduceOnly;
    private boolean isPostOnly;
    private String originator;

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

    protected void setMarket(String market) {
        this.market = market;
    }

    protected void setSide(String side) {
        this.side = side;
    }

    protected void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    protected void setType(String type) {
        this.type = type;
    }

    protected void setPrice(String price) {
        this.price = price;
    }

    protected void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    protected void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    protected void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    protected void setReduceOnly(boolean reduceOnly) {
        isReduceOnly = reduceOnly;
    }

    protected void setPostOnly(boolean postOnly) {
        isPostOnly = postOnly;
    }

    protected void setOriginator(String originator) {
        this.originator = originator;
    }
}
