package com.mj.market.app.dataprocessor;


import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


public class MarketDataProcessor{
    private List<SimpleResponseDto> requestObjects;
    private Set<PriceAlert> priceAlertsToNotify;
    private static final BigDecimal DEFAULT_PRICE_VALUE = BigDecimal.ZERO;


    public MarketDataProcessor(List<SimpleResponseDto> requestObjects) {
        this.requestObjects = requestObjects;
        this.priceAlertsToNotify = new CopyOnWriteArraySet();
    }

    public Set<PriceAlert> processing(Set<PriceAlert> priceAlerts) {
        ListIterator<PriceAlert> priceAlertsIter = priceAlerts.stream().toList().listIterator();
        String communicate = "Market price of pair: %s is %f and is %s than price %f in alert";

        while (priceAlertsIter.hasNext()) {
            PriceAlert priceAlert = priceAlertsIter.next();
            BigDecimal marketPrice = getApiResponseDtoPrice(priceAlert.getSymbol());

            if (checkIfMarketPriceIsOver(priceAlert, marketPrice)) {
                changePriceAlertStatus(priceAlert, communicate.formatted(priceAlert.getSymbol(), marketPrice, "higher", priceAlert.getMinPrice()));

            } else if (checkIfMarketPriceIsBelow(priceAlert, marketPrice)) {
                changePriceAlertStatus(priceAlert, communicate.formatted(priceAlert.getSymbol(), marketPrice, "lower", priceAlert.getMinPrice()));
            }
        }
        return priceAlertsToNotify;
    }

    private void changePriceAlertStatus(PriceAlert paObj, String communicate) {
        paObj.setActive(false);
        paObj.setCommunicate(communicate);
        priceAlertsToNotify.add(paObj);
    }

    private boolean checkIfMarketPriceIsBelow(PriceAlert paObj, BigDecimal stockPrice) {
        boolean marketPriceIsBelow = false;
        BigDecimal minPrice = paObj.getMinPrice();

        if (minPrice.compareTo(DEFAULT_PRICE_VALUE) != 0 && stockPrice.compareTo(minPrice) == -1) {
            marketPriceIsBelow = true;
        }
        return marketPriceIsBelow;
    }

    private boolean checkIfMarketPriceIsOver(PriceAlert paObj, BigDecimal stockPrice) {
        boolean marketPriceIsOver = false;
        BigDecimal maxPrice = paObj.getMaxPrice();

        if (maxPrice.compareTo(DEFAULT_PRICE_VALUE) != 0 && stockPrice.compareTo(maxPrice) == 1) {
            marketPriceIsOver = true;
        }
        return marketPriceIsOver;
    }

    private BigDecimal getApiResponseDtoPrice(Symbol symbol) {
        for (SimpleResponseDto obj : requestObjects) {
            if (obj.getCode().equals(symbol.getCode())) return obj.getPrice();
        }
        return DEFAULT_PRICE_VALUE;
    }
}

