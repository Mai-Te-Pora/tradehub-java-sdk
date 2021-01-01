package org.tradehub.domain.message.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;

@JsonPOJOBuilder
public class CreateOrderBuilder {

    @JsonProperty("market")
    String market;
    @JsonProperty("side")
    String side;
    @JsonProperty("quantity")
    String quantity;
    @JsonProperty("originator")
    String originator;
    @JsonProperty("type")
    String type = OrderType.LIMIT.value;
    @JsonProperty("price")
    String price;
    @JsonProperty("stop_price")
    String stopPrice;
    @JsonProperty("time_in_force")
    String timeInForce = OrderTimeInForce.GTC.value;
    @JsonProperty("trigger_type")
    String triggerType;
    @JsonProperty("is_reduce_only")
    boolean isReduceOnly = false;
    @JsonProperty("is_post_only")
    boolean isPostOnly = false;

    public CreateOrderBuilder withMarket(final String market){
        this.market = market;
        return this;
    }

    public CreateOrderBuilder withSide(final Side side){
        this.side = side.value;
        return this;
    }

    public CreateOrderBuilder withQuantity(final BigDecimal quantity){
        this.quantity = quantity.toPlainString();
        return this;
    }

    public CreateOrderBuilder withType(final OrderType type){
        this.type = type.value;
        return this;
    }

    public CreateOrderBuilder withPrice(final double price){
        this.price = String.valueOf(price);
        return this;
    }

    public CreateOrderBuilder withStopPrice(final double stopPrice){
        this.stopPrice = String.valueOf(stopPrice);
        return this;
    }

    public CreateOrderBuilder withOrderTimeInForce(final OrderTimeInForce timeInForce){
        this.timeInForce = timeInForce.value;
        return this;
    }

    public CreateOrderBuilder withTriggerType(final String triggerType){
        this.triggerType = triggerType;
        return this;
    }

    public CreateOrderBuilder withIsPostOnly(final boolean isPostOnly){
        this.isPostOnly = isPostOnly;
        return this;
    }

    public CreateOrderBuilder withIsReduceOnly(final boolean isReduceOnly){
        this.isReduceOnly = isReduceOnly;
        return this;
    }

    public CreateOrderBuilder withOriginator(final String originator){
        this.originator = originator;
        return this;
    }

    public CreateOrder build() {
        return new CreateOrder(this);
    }
}
