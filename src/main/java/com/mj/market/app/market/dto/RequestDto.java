package com.mj.market.app.market.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.math.BigInteger;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RequestDto {
    private long openTime;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private long closeTime;
    private BigDecimal quoteAssetVolume;
    private BigInteger NumberOfTrades;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyQuoteAssetVolume;
    private BigDecimal ignore;


    @Override
    public String toString() {
        return "RequestDto{" +
                "openTime=" + openTime +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                ", closePrice=" + closePrice +
                ", quoteAssetVolume=" + quoteAssetVolume +
                ", NumberOfTrades=" + NumberOfTrades +
                ", takerBuyBaseAssetVolume=" + takerBuyBaseAssetVolume +
                ", takerBuyQuoteAssetVolume=" + takerBuyQuoteAssetVolume +
                ", ignore=" + ignore +
                '}';
    }
}
