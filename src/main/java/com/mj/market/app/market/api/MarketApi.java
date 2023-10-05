package com.mj.market.app.market.api;

import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleRequestDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.symbol.Symbol;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public interface MarketApi {

    List<SimpleResponseDto> getAllPrices();
    SimpleRequestDto[] getPrices(Set<Symbol> symbols);
    List<ResponseDto> getDetailPriceHistory(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit);
}
