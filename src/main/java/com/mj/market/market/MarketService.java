package com.mj.market.market;

import com.mj.market.market.dto.ResponseDto;
import com.mj.market.market.dto.SimpleResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketService{

    private final CryptoMarketApi cryptoMarketApi;
   // private final MarketApi fiatMarketApi;


    public MarketService() {
       // fiatMarketApi = new BankierMarketAPI();
        cryptoMarketApi = new BinanceMarketApi();
    }

    public List<SimpleResponseDto> getAllPrices() {
        return cryptoMarketApi.getAllPrices();
    }

    public List<ResponseDto> getPriceDetails(String symbolStr, String intervalStr, String startLocalDateTime, String endLocalDateTime, String limitStr){

        //prepare input request
        BinanceSymbol symbol = BinanceSymbol.valueOf(symbolStr);
        Interval interval = Interval.valueOfLabel(intervalStr);
        LocalDateTime startDate = DateTime.toLocalDateTime(startLocalDateTime);
        LocalDateTime endDate = DateTime.toLocalDateTime(endLocalDateTime);
        int limit = Integer.valueOf(limitStr.trim());

        return cryptoMarketApi.getDetailPriceHistory(symbol, interval, startDate, endDate, limit);
    }
}

