package com.mj.market.app.pricealert;

public interface PriceAlertObservable {
    void notifyChangeInPriceAlertsCollection(PriceAlertsObserver observer);
}
