package com.mj.market.app.market.api;

import com.mj.market.app.market.dto.SimpleRequestDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.symbol.Symbol;

import java.util.List;
import java.util.Set;

public interface MarketApi {
    List<SimpleResponseDto> getAllPrices();
    SimpleRequestDto[] getPrices(Set<Symbol> symbols);
}
