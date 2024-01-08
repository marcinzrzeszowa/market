package com.mj.market.app.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@AllArgsConstructor
public class SimpleResponseSerialized implements Serializable {
    private String name;
    private String code;
    private BigDecimal price;

    @Override
    public String toString() {
        return String.format("%s %s %.3f\n", name, code, price);
    }



}
