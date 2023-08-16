package com.mj.market.market.proxy;

public class PriceAlertsReader implements PriceAlerts{
    private String PriceAlertRepository = "....read Alerts From Collection...";
    private static boolean isActual = false;

    @Override
    public String readAlerts() {
        String result = PriceAlertRepository;
        isActual = true;
        return result;
    }

    @Override
    public boolean isUpdated() {
        return isActual;
    }

    //from observable inter
    public static void changesInPriceAlerts(){
        isActual = false;
    }
}
