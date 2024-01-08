package com.mj.market.app.market.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
public class ResponseDto {

    private LocalDateTime openTime;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private LocalDateTime closeTime;
    private String chart;

    @Override
    public String toString() {
        return "ResponseDto{" +
                "openTime=" + openTime +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                ", closePrice=" + closePrice +
                '}';
    }

}
