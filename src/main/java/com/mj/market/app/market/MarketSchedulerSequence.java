package com.mj.market.app.market;
import com.mj.market.app.dataprocessor.MarketDataProcessor;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.notifier.UserAlertNotifier;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import com.mj.market.config.ColorConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class MarketSchedulerSequence {
    private static MarketDataProcessor marketDataProcessor;
    private static SymbolService symbolService;
    private static PriceAlertService priceAlertService;
    private static Set<Symbol> selectedSymbols = new HashSet<>();
    private final UserAlertNotifier userAlertNotifier;
    private static List<PriceAlert> priceAlerts;


    protected final String name;
    protected final Set<SymbolType> supportedSymbolType;
    private Set<Symbol> filteredSymbols;
    private Set<PriceAlert> priceAlertsToNotify;
    private List<SimpleResponseDto> simpleResponseDto;

    @Value("${logger.console.on}")
    private boolean LOGGER_ON;

    @Autowired
    public MarketSchedulerSequence(String apiName, PriceAlertService priceAlertService, SymbolService symbolService, UserAlertNotifier userNotifier) {
        this.name = apiName;
        this.priceAlertService = priceAlertService;
        this.symbolService = symbolService;
        this.userAlertNotifier = userNotifier;
        this.supportedSymbolType = setSupportedSymbolType();
    }

    protected abstract Set<SymbolType> setSupportedSymbolType();

    //Template method for every Market API. Use to check market prices in time stamp defined in MarketRequestScheduler class
    public final void startSimplePriceRequestSequence(){

        //Read user defined price alerts

        priceAlerts = readActiveUserAlerts();
        if(LOGGER_ON)ColorConsole.printlnYellow("1/4 : priceAlerts = "+ priceAlerts.toString());

        if(priceAlerts != null && !priceAlerts.isEmpty()){

            //Get set of interesting Symbols from all users alert list
            selectedSymbols = readDistinctTickersFromAlertList(priceAlerts);

            //Get symbols matching Market API implementation
            filteredSymbols = getSymbolsSupportedByMarketApi(selectedSymbols);
            if(LOGGER_ON)ColorConsole.printlnRed("2/4 : filteredSymbols = "+ filteredSymbols.toString());

            if(filteredSymbols != null && !filteredSymbols.isEmpty()){
                //get market symbols prices from API
                simpleResponseDto = requestPricesForScheduler(filteredSymbols);
                if(LOGGER_ON)ColorConsole.printlnGreen("3/4 : Response From: "+ name + " "+ simpleResponseDto.toString());

                //Analise prices and delegates calculations
                if(simpleResponseDto != null && !simpleResponseDto.isEmpty())
                    priceAlertsToNotify = marketDataProcessing(simpleResponseDto, getPriceAlertsBySymbolType(filteredSymbols));

                    saveChangesInPriceAlerts(priceAlertsToNotify);
                    if(priceAlertsToNotify != null && !priceAlertsToNotify.isEmpty())

                        if(LOGGER_ON)ColorConsole.printlnPurple("4/4 Go notify: " + priceAlertsToNotify.toString());
                        //notify user about price changes
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

    private Set<Symbol> getSymbolsSupportedByMarketApi(Set<Symbol> allSymbols){
       Set<Symbol> result = allSymbols.stream()
                .filter( e-> supportedSymbolType.contains(e.getType()))
                .collect(Collectors.toSet());
        return result;
    }
    protected Set<Symbol> getAllSymbolsSupportedByMarketApi() {
        Set<Symbol> allSymbols = getAllSymbols();
        return getSymbolsSupportedByMarketApi(allSymbols);
    }

    private Set<String> getSymbols(Set<Symbol>symbols){
        return symbols.stream()
                .map(e->e.getCode())
                .collect(Collectors.toSet());
    }

    private Set<PriceAlert> getPriceAlertsBySymbolType(Set<Symbol> allSymbols){
        return priceAlerts.stream().filter( pa -> allSymbols.contains(pa.getSymbol())).collect(Collectors.toSet());
    }

    private static Set<PriceAlert> marketDataProcessing(List<SimpleResponseDto> requestObjects, Set<PriceAlert> priceAlerts) {
        marketDataProcessor = new MarketDataProcessor(requestObjects);
       return marketDataProcessor.processing(priceAlerts);
    }
    private Set<Symbol> getAllSymbols(){
        return symbolService.getAllSymbols().stream().collect(Collectors.toSet());
    }

    private void notifyUser(Set<PriceAlert> priceAlertsToNotify) {
        userAlertNotifier.notifyUserAboutPriceChange(priceAlertsToNotify);
    }

    protected boolean checkIfSymbolIsValid(String code) {
        return symbolService.isSymbolByFormat(code);
    }


}
