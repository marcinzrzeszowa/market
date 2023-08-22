package com.mj.market.app.market;
import com.mj.market.app.email.EmailService;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertCache;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class MarketSchedulerSequence {

    protected Set<SymbolType> supportedSymbolType;
    private static Set<Symbol> selectedSymbols = new HashSet<>();
    private static Set<Symbol> filteredSymbols = new HashSet<>();
    private static DataAnalysis dataAnalysis = new DataAnalysis();
    private final PriceAlertCache priceAlertCache;
    private static List<PriceAlert> priceAlerts = new LinkedList<>();
    private static List<PriceAlert> priceAlertsToNotify = new LinkedList<>();
    private static List<SimpleResponseDto> simpleResponseDto = new LinkedList<>();


    private final EmailService emailService;
    private final SymbolService symbolService;


    @Autowired
    public MarketSchedulerSequence(PriceAlertCache priceAlertCache, EmailService emailService, SymbolService symbolService, Set<SymbolType> supportedSymbolType) {
        this.priceAlertCache = priceAlertCache;
        this.emailService = emailService;
        this.symbolService = symbolService;
        this.supportedSymbolType = supportedSymbolType;
    }

    //Template method for every Market API
    public final void startSimplePriceRequestSequence(){

        //Read user defined price alerts
        priceAlerts = readActiveUserAlerts();

        //Get set of interesting Symbols from users alert list
        selectedSymbols = readDistinctTickersFromAlertList(priceAlerts);

        //Get just symbols valid for Market API implementation
        filteredSymbols = getFilteredSymbols(selectedSymbols);

        //get market symbols prices from API
        simpleResponseDto = requestPricesForScheduler(filteredSymbols);

        //Analise prices and delegate calculations
        priceAlertsToNotify = marketDataAnalysis(simpleResponseDto, priceAlerts);

        if(priceAlertsToNotify != null){

            //change status active to not active
            changePriceAlertsStatus(priceAlertsToNotify);

            //notify user ba sending email
            notifyUser(priceAlertsToNotify);
        }
    }

    private List<PriceAlert> readActiveUserAlerts() {
        boolean getJustActivePriceAlerts = true;
        return priceAlertCache.findByIsActive(getJustActivePriceAlerts);
    }

    private static Set<Symbol> readDistinctTickersFromAlertList(List<PriceAlert> priceAlertsList) {
        Set<Symbol> set = new HashSet<>();
        for(PriceAlert alert: priceAlertsList){
                set.add(alert.getSymbol());
        }
        return set;
    }
    protected abstract List<SimpleResponseDto> requestPricesForScheduler(Set<Symbol> marketSymbols);

    private Set<Symbol> getFilteredSymbols(Set<Symbol> allSymbols){
       Set<Symbol> result = allSymbols.stream()
                .filter( e-> supportedSymbolType.contains( e.getType()))
                .collect(Collectors.toSet());
        return result;
    }


    private static List<PriceAlert> marketDataAnalysis(List<SimpleResponseDto> requestObjects, List<PriceAlert> priceAlerts) {
       // ColorConsole.printlnYellow("readDistinctTickersFromAlertList()");
       return dataAnalysis.analyse(requestObjects, priceAlerts);
    }

    private static void changePriceAlertsStatus(List<PriceAlert> priceAlertsToNotify) {
        //ColorConsole.printlnCyan("changePriceAlertsStatus()");
    }

    private static void notifyUser(List<PriceAlert> priceAlertsToNotify) {
        //ColorConsole.printlnWhite("notifyUser()");
            //TODO sent email with notification
    }

    protected Set<String> getAllSymbolCodes() {
        return symbolService.getAllSymbolFormats();
    }

    protected String getSymbolByCode(String code) {
        return symbolService.getSymbolsByCode(code);
    }

    protected List<String> getSupportedSymbolCodes(Set<String> allSymbols){
        return symbolService.getSymbolsByCode(allSymbols);
    }


}
