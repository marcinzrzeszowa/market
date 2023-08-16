package com.mj.market.market.main;

import com.mj.market.market.dto.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public abstract class MarketApiRequestSequence{
    private static DataAnalysis dataAnalysis;
    private static List<String> priceAlerts = new LinkedList<>();
    protected static Set<String> marketSymbols = new TreeSet<>();
    private static List<SimpleResponseDto> simpleObjects = new LinkedList<>();
    private static List<String> priceAlertsToNotify = new LinkedList<>();


    public MarketApiRequestSequence() {
        this.dataAnalysis = new DataAnalysis();
    }

    //Template method for every Market API
    public final void startSimplePriceRequestSequence(){

        //Read user defined price alerts
        priceAlerts = readUserAlerts();

        //Get set of interesting Symbols from users alert list
        marketSymbols = readDistinctTickersFromAlertList(priceAlerts);

        //get market symbols prices from Market API
        simpleObjects = requestPrices(marketSymbols);

        //Analise prices and delegate calculations
        priceAlertsToNotify = marketDataAnalysis(simpleObjects, priceAlerts);

        if(priceAlertsToNotify != null){

            //change status active to not active
            changePriceAlertsStatus(priceAlertsToNotify);

            //notify user ba sending email
            notifyUser(priceAlertsToNotify);
        }
    }

    private static List<String> readUserAlerts() {
       // ColorConsole.printlnRed("readUserAlerts()");
        //TODO proxy cache, zmienic Typ na PriceAlert
        return null;
    }

    private static Set<String> readDistinctTickersFromAlertList(List<String> cachePriceAlertList) {
       // ColorConsole.printlnGreen("readDistinctTickersFromAlertList()");
    //TODO get SYmbols Tickers from alert list
        return Set.of("BTCUSDT","ETHUSDT");
    }

    protected abstract List<SimpleResponseDto> requestPrices(Set<String> marketSymbols);

    private static List<String> marketDataAnalysis(List<SimpleResponseDto> requestObjects, List<String> priceAlerts) {
       // ColorConsole.printlnYellow("readDistinctTickersFromAlertList()");
       return dataAnalysis.analyse(requestObjects, priceAlerts);
    }

    private static void changePriceAlertsStatus(List<String> priceAlertsToNotify) {
        //ColorConsole.printlnCyan("changePriceAlertsStatus()");
    }

    private static void notifyUser(List<String> priceAlertsToNotify) {
        //ColorConsole.printlnWhite("notifyUser()");
            //TODO sent email with notification
    }

}
