package com.mj.market.app.pricealert;

import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PriceAlertService implements PriceAlertObservable{

    public final PriceAlertRepository priceAlertRepository;
    private final PriceAlertObserver priceAlertObserver;
    private final SymbolService symbolService;

    @Autowired
    public PriceAlertService(PriceAlertRepository priceAlertRepository, PriceAlertObserver priceAlertObserver, SymbolService stockTickerService) {
        this.priceAlertRepository = priceAlertRepository;
        this.priceAlertObserver = priceAlertObserver;
        this.symbolService = stockTickerService;
    }

    public PriceAlert findById(Long id){
        return priceAlertRepository.findById(id).get();
    }
    public List<PriceAlert> readAllPriceAlerts(){
        return priceAlertRepository.findAll();
    }

    public List<PriceAlert> readUserPriceAlerts(Long id){
        return priceAlertRepository.findByUserId(id);
    }


    public void savePriceAlert(PriceAlert priceAlert) {
        priceAlertRepository.save(priceAlert);
        notifyChangeInPriceAlertsList(priceAlertObserver);
    }

    public void deletePriceAlert(Long id){
        if(priceAlertRepository.existsById(id)){
            priceAlertRepository.deleteById(id);
            notifyChangeInPriceAlertsList(priceAlertObserver);
        }
    }

    public List<Symbol> getSymbols(){
        return symbolService.getAllSymbols();
    }

    public PriceAlert updatePriceAlert(Long id, PriceAlert alert) {
        PriceAlert priceAlert = priceAlertRepository.findById(id)
                .map(element->{
                    element.setSymbol(alert.getSymbol());
                    element.setDescription(alert.getDescription());
                    element.setMaxPrice(alert.getMaxPrice());
                    element.setMinPrice(alert.getMinPrice());
                    element.setActive(alert.getActive());
                    element.setRelatedAlertId((alert.getRelatedAlertId()));
                    return priceAlertRepository.save(element);
                }).orElseThrow(()->new IllegalStateException());
        notifyChangeInPriceAlertsList(null);

        notifyChangeInPriceAlertsList(priceAlertObserver);
        return priceAlert;
    }

    @Override
    public void notifyChangeInPriceAlertsList(PriceAlertObserver observer) {
        observer.setNotCurrentPriceAlertsList();
    }

}
