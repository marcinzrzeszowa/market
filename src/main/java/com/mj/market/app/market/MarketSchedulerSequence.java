package com.mj.market.app.market;
import com.mj.market.app.dataprocessor.MarketDataProcessor;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertsCache;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import com.mj.market.config.ColorConsole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public abstract class MarketSchedulerSequence {
    private static MarketDataProcessor marketDataProcessor;
    private static SymbolService symbolService;
    private static Set<Symbol> selectedSymbols = new HashSet<>();
    private static PriceAlertsCache priceAlertCache;
    private static List<PriceAlert> priceAlerts;


    protected final String name;
    protected Set<SymbolType> supportedSymbolType;
    private Set<Symbol> filteredSymbols;
    private Set<PriceAlert> priceAlertsToNotify;
    private List<SimpleResponseDto> simpleResponseDto;

    private static final boolean loggerEnable = true;

    @Autowired
    public MarketSchedulerSequence(String apiName, PriceAlertsCache priceAlertCache, SymbolService symbolService) {
        this.name = apiName;
        this.priceAlertCache = priceAlertCache;
        this.symbolService = symbolService;
        this.supportedSymbolType = setSupportedSymbolType();
    }

    protected abstract Set<SymbolType> setSupportedSymbolType();

    //Template method for every Market API
    public final void startSimplePriceRequestSequence(){

        //Read user defined price alerts
        //TODO Query n
        priceAlerts = readActiveUserAlerts();
        if(loggerEnable)ColorConsole.printlnYellow("priceAlerts = "+ priceAlerts.toString());

        if(priceAlerts != null){

            //Get set of interesting Symbols from all users alert list
            selectedSymbols = readDistinctTickersFromAlertList(priceAlerts);

            //Get symbols matching Market API implementation
            filteredSymbols = getSymbolsByType(selectedSymbols);
            if(loggerEnable)ColorConsole.printlnRed("filteredSymbols = "+ filteredSymbols.toString());

            if(filteredSymbols != null && !filteredSymbols.isEmpty()){
                //get market symbols prices from API
                simpleResponseDto = requestPricesForScheduler(filteredSymbols);
                if(loggerEnable)ColorConsole.printlnGreen("Response From: "+ name + " "+ simpleResponseDto.toString());

                //Analise prices and delegate calculations
                if(simpleResponseDto != null)
                    priceAlertsToNotify = marketDataProcessing(simpleResponseDto, getPriceAlertsBySymbolType(filteredSymbols));
                    if(loggerEnable)ColorConsole.printlnPurple("To notify: " + priceAlertsToNotify.toString());

                    if(priceAlertsToNotify != null){
                    //notify user ba sending email
                    notifyUser(priceAlertsToNotify);
                }
            }
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

    private static void notifyUser(Set<PriceAlert> priceAlertsToNotify) {
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
