package com.mj.market.app.dataprocessor;


import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.symbol.Symbol;

import java.math.BigDecimal;
import java.util.*;



public class MarketDataProcessor {
    private List<SimpleResponseDto> requestObjects;
    private Set<PriceAlert> priceAlertsToNotify;
    private static final BigDecimal DEFAULT_PRICE_VALUE = BigDecimal.ZERO;

    public MarketDataProcessor(List<SimpleResponseDto> requestObjects) {
        this.requestObjects = requestObjects;
        this.priceAlertsToNotify = new HashSet<>();
    }

    public void analise(){

        //command

    }

    public Set<PriceAlert> performDataAnalise(Set<PriceAlert> priceAlerts) {


        ListIterator<PriceAlert> priceAlertsIter = priceAlerts.stream().toList().listIterator();
        while(priceAlertsIter.hasNext()){
            PriceAlert paObj = priceAlertsIter.next();
            BigDecimal maxPrice = paObj.getMaxPrice();
            BigDecimal minPrice = paObj.getMinPrice();
            BigDecimal stockPrice = getApiResponseDtoPrice(paObj.getSymbol());
            BigDecimal subtract, dividing;

            if(maxPrice.compareTo(DEFAULT_PRICE_VALUE) != 0 && stockPrice.compareTo(maxPrice) == 1){
                // market price > max price value
                subtract = stockPrice.subtract(maxPrice);
                //paObj.setCommunicate("Market price of pair is" + subtract +" higher then defined prices in alert");
                paObj.setActive(false);
                priceAlertsToNotify.add(paObj);
            }
            else if(minPrice.compareTo(DEFAULT_PRICE_VALUE) != 0  && stockPrice.compareTo(minPrice) == -1) {
                // market price < min price value
                subtract = minPrice.subtract(stockPrice);
                //paObj.setCommunicate("Market price of pair is "+ subtract +" lover then defined prices in alert");
                paObj.setActive(false);
                priceAlertsToNotify.add(paObj);
            }
        }
        return priceAlertsToNotify;
    }


    private BigDecimal getApiResponseDtoPrice(Symbol symbol) {
        for(SimpleResponseDto obj: requestObjects){
            if(obj.getCode().equals(symbol.getCode())) return obj.getPrice();
        }
        return DEFAULT_PRICE_VALUE;
    }
}
