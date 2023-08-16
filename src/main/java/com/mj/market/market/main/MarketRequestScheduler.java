package com.mj.market.market.main;

import com.mj.market.market.BinanceMarketApi;
import com.mj.market.market.config.ColorConsole;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketRequestScheduler {
    private static final long second = 1000l;
    private static final long minute = 1000l*60;
    private static final long hour = minute*60;
    private static final long day = hour*24;


    @Scheduled(fixedRate = 1 * second) // zmienic na 5 min
    private void marketScheduler5m(){
        System.out.println();
        ColorConsole.printlnBlue("marketScheduler5m: +++++++++++++++++++++++++"  + Thread.currentThread().getName());
        MarketApiRequestSequence requestSequence = new BinanceMarketApi();
        requestSequence.startSimplePriceRequestSequence();
        ColorConsole.printlnBlue("+++++++++++++++++++++++++++++++++++++++++++");

    }

    @Scheduled(fixedRate = 3 * second) //zmienic na 1 na dzien
    private void marketScheduler1D(){
        ColorConsole.printlnGreen("marketScheduler1D" + Thread.currentThread().getName());
    }

}
