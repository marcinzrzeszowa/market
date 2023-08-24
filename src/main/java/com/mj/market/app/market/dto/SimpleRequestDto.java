package com.mj.market.app.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SimpleRequestDto implements RequestObject {

        private String symbol;
        private BigDecimal price;

    @Override
    public String toString() {
        return "SimpleRequestDto{symbol=" + symbol + ", price=" + price +"}" ;
    }
}
