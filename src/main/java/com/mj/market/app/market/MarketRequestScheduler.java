package com.mj.market.app.market;

import com.mj.market.config.ColorConsole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketRequestScheduler {
    private static final long second = 1000l;
    private static final long minute = 1000l*60;
    private static final long hour = minute*60;
    private static final long day = hour*24;

    private final MarketSchedulerSequence cryptoMarketSequence;
    //TODO add Bankier API
    private final MarketSchedulerSequence fiatMarketSequence;

    public MarketRequestScheduler(@Qualifier("BinanceMarketApi") MarketSchedulerSequence cryptoMarketSequence,
                                  @Qualifier("BankierMarketApi") MarketSchedulerSequence fiatMarketSequence) {
        this.cryptoMarketSequence = cryptoMarketSequence;
        this.fiatMarketSequence = fiatMarketSequence;
    }

    @Scheduled(fixedRate = 3 * second, initialDelay = 2 * second) // zmienic na 5 min
    private void marketScheduler5m(){

        ColorConsole.printlnBlue(Thread.currentThread().getName());
        cryptoMarketSequence.startSimplePriceRequestSequence();
        ColorConsole.printlnBlue("+++");

    }

    @Scheduled(fixedRate = 3 * day, initialDelay = 2 * second) //zmienic na 1 na dzien
    private void marketScheduler1D(){
        //ColorConsole.printlnGreen(Thread.currentThread().getName());
        //fiatMarketSequence.startSimplePriceRequestSequence();
       // ColorConsole.printlnGreen("+++");
    }

}
