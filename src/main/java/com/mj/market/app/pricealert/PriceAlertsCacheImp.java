package com.mj.market.app.pricealert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAlertsCacheImp implements PriceAlertsCache, PriceAlertObserver {
    private static boolean isCurrentAlertList = false;
    private static List<PriceAlert> priceAlerts;
    private final PriceAlertRepository priceAlertRepository;

    @Autowired
    public PriceAlertsCacheImp(PriceAlertRepository priceAlertRepository) {
        this.priceAlertRepository = priceAlertRepository;
    }

    @Override
    public List<PriceAlert> findByIsActive(boolean isActive) {
        if (!isCurrentAlertList) {
            priceAlerts = priceAlertRepository.findByIsActive(isActive);
            isCurrentAlertList = true;
        }
        return priceAlerts;
    }

    @Override
    public List<PriceAlert> findAll() {
        if (!isCurrentAlertList) {
            priceAlerts = priceAlertRepository.findAll();
            isCurrentAlertList = true;
        }
        return priceAlerts;
    }

    @Override
    public void setNotCurrentPriceAlertsList() {
        isCurrentAlertList = false;

        //TODO find by is active and swith off in list of alerts
    }
}
