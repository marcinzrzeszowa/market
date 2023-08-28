package com.mj.market.app.market;

import com.mj.market.app.DateTime;
import com.mj.market.app.market.api.Interval;
import com.mj.market.app.market.api.MarketApi;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketService{

    private final MarketApi cryptoMarketApi;
   // private final MarketApi fiatMarketApi;


    public MarketService(@Qualifier("BinanceMarketApi") MarketApi cryptoMarketApi) {
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

        //TODO for Apis
        return cryptoMarketApi.getAllPrices();
    }
}

