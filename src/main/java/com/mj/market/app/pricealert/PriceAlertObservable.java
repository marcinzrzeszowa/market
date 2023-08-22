package com.mj.market.app.pricealert;

public interface PriceAlertObservable {
    void notifyChangeInPriceAlertsList(PriceAlertObserver observer);
}
