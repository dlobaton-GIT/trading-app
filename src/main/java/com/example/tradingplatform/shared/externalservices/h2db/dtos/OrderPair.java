package com.example.tradingplatform.shared.externalservices.h2db.dtos;

import com.example.tradingplatform.shared.externalservices.h2db.model.Orders;

public class OrderPair {
    private Orders buyOrder;
    private Orders sellOrder;

    public Orders getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Orders buyOrder) {
        this.buyOrder = buyOrder;
    }

    public Orders getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Orders sellOrder) {
        this.sellOrder = sellOrder;
    }
}
