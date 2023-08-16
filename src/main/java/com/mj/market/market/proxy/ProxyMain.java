package com.mj.market.market.proxy;

public class ProxyMain {
    private static PriceAlerts priceAlerts = new PriceAlertsCacheReader();

    public static void main(String[] args) {

        String alerts = priceAlerts.readAlerts();
        System.out.println("Load alerts first time:"+ alerts);

        alerts = priceAlerts.readAlerts();
        System.out.println("Load alerts second time:"+ alerts);

        //simulate changes in collection
        PriceAlertsReader.changesInPriceAlerts();

        alerts = priceAlerts.readAlerts();
        System.out.println("Load alerts third time:"+ alerts);
    }

}
