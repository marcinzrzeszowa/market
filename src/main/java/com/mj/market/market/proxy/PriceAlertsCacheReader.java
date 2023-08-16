package com.mj.market.market.proxy;

public class PriceAlertsCacheReader implements PriceAlerts{
    private PriceAlerts priceAlertsReader = new PriceAlertsReader();;
    private String priceAlertsCache;


    @Override
    public String readAlerts() {
        if(priceAlertsReader.isUpdated() == false){
            String result = priceAlertsReader.readAlerts();
            priceAlertsCache = result + " from cache";
            return result;
        }
        return priceAlertsCache;
    }

    @Override
    public boolean isUpdated() {
        return priceAlertsReader.isUpdated();
    }
}
