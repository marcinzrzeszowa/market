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

    private final MarketSchedulerSequence cryptoMarketSchedulerSequence;
    //TODO add Bankier API
    private final MarketSchedulerSequence cryptoMarketSchedulerSequenceSecond;

    public MarketRequestScheduler(@Qualifier("BinanceMarketApi") MarketSchedulerSequence cryptoMarketSchedulerSequence,
                                  @Qualifier("BinanceMarketApi") MarketSchedulerSequence cryptoMarketSchedulerSequenceSecond) {
        this.cryptoMarketSchedulerSequence = cryptoMarketSchedulerSequence;
        this.cryptoMarketSchedulerSequenceSecond = cryptoMarketSchedulerSequenceSecond;
    }

    @Scheduled(fixedRate = 3 * second) // zmienic na 5 min
    private void marketScheduler5m(){

        ColorConsole.printlnBlue(Thread.currentThread().getName());
        cryptoMarketSchedulerSequence.startSimplePriceRequestSequence();
        ColorConsole.printlnBlue("+++");

    }

    @Scheduled(fixedRate = 3 * day) //zmienic na 1 na dzien
    private void marketScheduler1D(){
        ColorConsole.printlnGreen(Thread.currentThread().getName());

        //cryptoMarketSchedulerSequenceSecond.startSimplePriceRequestSequence();
        ColorConsole.printlnGreen("+++");
    }

}
