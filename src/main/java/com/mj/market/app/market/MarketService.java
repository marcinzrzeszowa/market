package com.mj.market.app.market;

import com.mj.market.config.DateTime;
import com.mj.market.app.market.api.Interval;
import com.mj.market.app.market.api.CryptoMarketApi;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class MarketService{

    private final CryptoMarketApi cryptoMarketApi;
   // private final MarketApi fiatMarketApi;

    @Autowired
    public MarketService(@Qualifier("BinanceMarketApi") CryptoMarketApi cryptoMarketApi) {
        this.cryptoMarketApi = cryptoMarketApi;
    }

    public List<SimpleResponseDto> getAllPrices() {
        return getAllPricesByMarketApi();
    }

    public List<ResponseDto> getPriceDetails(String symbolStr, String intervalStr, String startLocalDateTime, String endLocalDateTime, String limitStr){

        Interval interval = Interval.valueOfLabel(intervalStr);
        LocalDateTime startDate = DateTime.toLocalDateTime(startLocalDateTime);
        LocalDateTime endDate = DateTime.toLocalDateTime(endLocalDateTime);
        int limit = Integer.valueOf(limitStr.trim());

        return cryptoMarketApi.getDetailPriceHistory(symbolStr, interval, startDate, endDate, limit);
    }

    private List<SimpleResponseDto> getAllPricesByMarketApi() {
        List<SimpleResponseDto> aggregatedPriceAlerstsList = new LinkedList<>();

        //TODO add FIAT
        aggregatedPriceAlerstsList.addAll(cryptoMarketApi.getAllPrices());


        return aggregatedPriceAlerstsList;
    }
}

