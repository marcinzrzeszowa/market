package com.mj.market.app.pricealert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAlertCacheImp implements PriceAlertCache {
    private static boolean isCurrentAlertList = false;
    private static List<PriceAlert> priceAlerts;
    private final PriceAlertRepository priceAlertRepository;

    @Autowired
    public PriceAlertCacheImp(PriceAlertRepository priceAlertRepository) {
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

  /*  @Override
    public void setNotCurrentPriceAlertsList() {
        isCurrentAlertList = false;
        System.out.println("+++++++++  isCurrentAlertList = false;");
    }*/
}
