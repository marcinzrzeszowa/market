package com.mj.market.app.pricealert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class PriceAlertsCacheClass implements PriceAlertsCache, PriceAlertsObserver {
    private static boolean isActualAlertsList = false;
    private static Vector<PriceAlert> priceAlerts;
    private final PriceAlertRepository priceAlertRepository;

    @Autowired
    public PriceAlertsCacheClass(PriceAlertRepository priceAlertRepository) {
        this.priceAlertRepository = priceAlertRepository;
    }

    @Override
    public Vector<PriceAlert> findByIsActive(boolean isActive) {
        if (!isActualAlertsList) {
            priceAlerts = priceAlertRepository.findByIsActive(isActive);
            if(priceAlerts != null) isActualAlertsList = true;
        }
        return priceAlerts;
    }
    @Override
    public void setNotActualPriceAlertsCollection() {
        isActualAlertsList = false;
    }
}
