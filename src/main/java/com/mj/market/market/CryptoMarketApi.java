package com.mj.market.market;

import com.mj.market.market.dto.ResponseDto;
import com.mj.market.market.dto.SimpleResponseDto;

import java.time.LocalDateTime;
import java.util.List;


public interface CryptoMarketApi {

    List<SimpleResponseDto> getAllPrices();
    List<SimpleResponseDto> getPrices(List<BinanceSymbol> symbols);
    List<ResponseDto> getDetailPriceHistory(BinanceSymbol symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit);

}
