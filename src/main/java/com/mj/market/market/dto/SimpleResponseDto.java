package com.mj.market.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class SimpleResponseDto implements ResponseObject{

        private String symbol;
        private BigDecimal price;

    @Override
    public String toString() {
        return "SymbolDto{symbol=" + symbol + ", price=" + price +"}" ;
    }
}
