package com.mj.market.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SimpleRequestDto implements RequestObject {

        private String symbol;
        private BigDecimal price;

    @Override
    public String toString() {
        return "SymbolDto{symbol=" + symbol + ", price=" + price +"}" ;
    }
}
