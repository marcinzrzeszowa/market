package com.mj.market.app.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
public class SimpleResponseDto implements ResponseObject{

        private String name;
        private String code;
        private BigDecimal price;

    public SimpleResponseDto(String name, String code, BigDecimal price) {
        this.name = name;
        this.code = code;
        this.price = price;
    }

    @Override
    public String toString() {
        return "SimpleResponseDto{" + "name='" + name   + ", price=" + price + '}';
    }
}
