package com.mj.market.app.market;


import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlert;
import org.springframework.stereotype.Component;

import java.util.List;

public class DataAnalysis {

    public List<PriceAlert> analyse(List<SimpleResponseDto> requestObjects, List<PriceAlert> priceAlerts) {
        printObjects(requestObjects);
        return null;
    }

    private void printObjects(List<SimpleResponseDto> requestObjects){
        for (SimpleResponseDto e: requestObjects){
            System.out.println(e.toString());
        }
    }

    /*private void comparePricesAndSentNotifications(List<StockApiWrapper> stocksList, List<PriceAlert> priceAlertsList) {

        ArrayList<PriceAlert>listOfPriceAlertsToBeSent = new ArrayList<>();
        Map<Long,BigDecimal> currentPrices = new HashMap<>();

        for(StockApiWrapper stockElement: stocksList){
            for (PriceAlert priceAlert: priceAlertsList){
                if(stockElement.getSymbol().equals(priceAlert.getTicker().getSymbol())){
                    BigDecimal stockPrice = stockElement.getPrice().setScale(3, RoundingMode.CEILING);
                    BigDecimal maxPrice = priceAlert.getMaxPrice();
                    BigDecimal minPrice = priceAlert.getMinPrice();

                    if(maxPrice.compareTo(BigDecimal.ZERO) !=0 && stockPrice.compareTo(maxPrice) == 1 ) {
                        listOfPriceAlertsToBeSent.add(priceAlert);
                        currentPrices.put(priceAlert.getId(),stockElement.getPrice());
                    }else if(minPrice.compareTo(BigDecimal.ZERO) !=0  && stockPrice.compareTo(minPrice) == -1){
                        if(!listOfPriceAlertsToBeSent.contains(priceAlert)){
                            listOfPriceAlertsToBeSent.add(priceAlert);
                            currentPrices.put(priceAlert.getId(),stockElement.getPrice());
                        }
                    }
                }
            }
        }
        if(!listOfPriceAlertsToBeSent.isEmpty() && activeSendNotification){
            sendNotification(listOfPriceAlertsToBeSent, currentPrices);
        }
    }*/
}
