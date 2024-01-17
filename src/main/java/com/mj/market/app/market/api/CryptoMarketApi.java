package com.mj.market.app.market.api;
import com.mj.market.app.market.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CryptoMarketApi extends MarketApi {
    List<ResponseDto> getDetailPriceHistory(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit);
}
