package com.mj.market.app.market;

import com.mj.market.app.config.ColorConsole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    private static final long second = 1000l;
    private static final long minute = 1000l*60;
    private static final long hour = minute*60;
    private static final long day = hour*24;

    @Value("${logger.console.on}")
    private boolean LOGGER_ON;


    private final MarketScheduler cryptoMarketSequence;
    //TODO add Bankier API
    private final MarketScheduler fiatMarketSequence;

    public Scheduler(@Qualifier("BinanceMarketApi") MarketScheduler cryptoMarketSequence,
                     @Qualifier("BankerMarketApi") MarketScheduler fiatMarketSequence) {
        this.cryptoMarketSequence = cryptoMarketSequence;
        this.fiatMarketSequence = fiatMarketSequence;
    }

    @Scheduled(fixedRate = 5 * minute, initialDelay = 2 * minute) // zmienic na 5 min
    private void marketScheduler5m(){

        if(LOGGER_ON)ColorConsole.printlnBlue(Thread.currentThread().getName());
        cryptoMarketSequence.startSimplePriceRequestSequence();
        if(LOGGER_ON)ColorConsole.printlnBlue("+++");

    }

  /*  @Scheduled(fixedRate = 3 * day, initialDelay = 2 * second) //zmienic na 1 na dzien
    private void marketScheduler1D(){
        //ColorConsole.printlnGreen(Thread.currentThread().getName());
        //fiatMarketSequence.startSimplePriceRequestSequence();
       // ColorConsole.printlnGreen("+++");
    }*/

}
