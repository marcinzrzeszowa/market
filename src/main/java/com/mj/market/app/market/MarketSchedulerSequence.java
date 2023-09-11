package com.mj.market.app.market;
import com.mj.market.app.dataprocessor.MarketDataProcessor;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.notifier.UserNotifier;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import com.mj.market.config.ColorConsole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public abstract class MarketSchedulerSequence {
    private static MarketDataProcessor marketDataProcessor;
    private static SymbolService symbolService;
    private static PriceAlertService priceAlertService;
    private static Set<Symbol> selectedSymbols = new HashSet<>();
    private final UserNotifier userNotifier;
    private static List<PriceAlert> priceAlerts;


    protected final String name;
    protected Set<SymbolType> supportedSymbolType;
    private Set<Symbol> filteredSymbols;
    private Set<PriceAlert> priceAlertsToNotify;
    private List<SimpleResponseDto> simpleResponseDto;

    private static final boolean loggerEnable = true;

    @Autowired
    public MarketSchedulerSequence(String apiName, PriceAlertService priceAlertService, SymbolService symbolService, UserNotifier userNotifier) {
        this.name = apiName;
        this.priceAlertService = priceAlertService;
        this.symbolService = symbolService;
        this.userNotifier = userNotifier;
        this.supportedSymbolType = setSupportedSymbolType();
    }

    protected abstract Set<SymbolType> setSupportedSymbolType();

    //Template method for every Market API. Use to check market prices in time stamp defined in MarketRequestScheduler class
    public final void startSimplePriceRequestSequence(){

        //Read user defined price alerts

        priceAlerts = readActiveUserAlerts();
        if(loggerEnable)ColorConsole.printlnYellow("1/4 : priceAlerts = "+ priceAlerts.toString());

        if(priceAlerts != null && !priceAlerts.isEmpty()){

            //Get set of interesting Symbols from all users alert list
            selectedSymbols = readDistinctTickersFromAlertList(priceAlerts);

            //Get symbols matching Market API implementation
            filteredSymbols = getSymbolsByType(selectedSymbols);
            if(loggerEnable)ColorConsole.printlnRed("2/4 : filteredSymbols = "+ filteredSymbols.toString());

            if(filteredSymbols != null && !filteredSymbols.isEmpty()){
                //get market symbols prices from API
                simpleResponseDto = requestPricesForScheduler(filteredSymbols);
                if(loggerEnable)ColorConsole.printlnGreen("3/4 : Response From: "+ name + " "+ simpleResponseDto.toString());

                //Analise prices and delegates calculations
                if(simpleResponseDto != null)
                    priceAlertsToNotify = marketDataProcessing(simpleResponseDto, getPriceAlertsBySymbolType(filteredSymbols));
                    if(loggerEnable)ColorConsole.printlnPurple("4/4 Go notify: " + priceAlertsToNotify.toString());

                    saveChangesInPriceAlerts(priceAlertsToNotify);

                    if(priceAlertsToNotify != null)

                        //notify user ba sending message
                        notifyUser(priceAlertsToNotify);
            }
        }
    }

    private void saveChangesInPriceAlerts(Set<PriceAlert> priceAlertsToNotify) {
        priceAlertsToNotify.stream().forEach(e->priceAlertService.savePriceAlert(e));
    }


    private List<PriceAlert> readActiveUserAlerts() {
        boolean getJustActivePriceAlerts = true;
        return priceAlertService.getPriceAlertsCache().findByIsActive(getJustActivePriceAlerts);
    }

    private static Set<Symbol> readDistinctTickersFromAlertList(List<PriceAlert> priceAlertsList) {
        Set<Symbol> set = new HashSet<>();
        for(PriceAlert alert: priceAlertsList){
                set.add(alert.getSymbol());
        }
        return set;
    }
    protected abstract List<SimpleResponseDto> requestPricesForScheduler(Set<Symbol> marketSymbols);

    private Set<Symbol> getSymbolsByType(Set<Symbol> allSymbols){
       Set<Symbol> result = allSymbols.stream()
                .filter( e-> supportedSymbolType.contains(e.getType()))
                .collect(Collectors.toSet());
        return result;
    }
    private Set<PriceAlert> getPriceAlertsBySymbolType(Set<Symbol> allSymbols){
        return priceAlerts.stream().filter( pa -> allSymbols.contains(pa.getSymbol())).collect(Collectors.toSet());
    }

    private static Set<PriceAlert> marketDataProcessing(List<SimpleResponseDto> requestObjects, Set<PriceAlert> priceAlerts) {
        marketDataProcessor = new MarketDataProcessor(requestObjects);
       return marketDataProcessor.processing(priceAlerts);
    }

    private void notifyUser(Set<PriceAlert> priceAlertsToNotify) {
        userNotifier.notify(priceAlertsToNotify);
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
