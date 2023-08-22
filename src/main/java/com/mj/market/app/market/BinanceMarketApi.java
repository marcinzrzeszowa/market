package com.mj.market.app.market;

import com.mj.market.app.DateTime;
import com.mj.market.app.email.EmailService;
import com.mj.market.app.market.dto.ObjectMapper;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleRequestDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlertCache;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("BinanceMarketApi")
public class BinanceMarketApi extends MarketSchedulerSequence implements MarketApi {
    private RestTemplate restTemplate = new RestTemplate();


    //API URLs
    private static final String URL_API = "https://api.binance.com/api/v3";
    private static final String URL_SIMPLE_PRICES= URL_API + "/ticker/price?symbols=";
    private static final String URL_PRICE =  URL_API + "/klines?symbol=";
    private final static String INTERVAL = "&interval=";
    private final static String LIMIT = "&limit=";
    private final static String START_TIME = "&startTime=";
    private final static String END_TIME = "&endTime=";

    public BinanceMarketApi(PriceAlertCache priceAlertCache, EmailService emailService, SymbolService symbolService) {
        super(priceAlertCache, emailService, symbolService,
                new HashSet<>(SymbolType.KRYPTOWALUTA.ordinal())
                );
    }

    @Override
    public List<SimpleResponseDto> getAllPrices(){
        return getPrices(super.getAllSymbolCodes());
    }


    public List<SimpleResponseDto> getPrices(Set<String> symbols) {
        List<String> supportedSymbolCodes = super.getSupportedSymbolCodes(symbols);
        String url= buildMultiSimpleSymbolsUrl(supportedSymbolCodes);
        SimpleRequestDto[] objArray= getRequestObjFromMarketApi(SimpleRequestDto[].class, url);
        if(objArray == null) return new LinkedList<>();
        return ObjectMapper.mapToSimpleResponseDtoList(objArray);
    }


    @Override
    public List<ResponseDto> getDetailPriceHistory(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit){
        if(symbol == null) throw new IllegalArgumentException();
        if(!getSymbolByCode(symbol).equals(symbol)) throw new IllegalArgumentException();

        String url = buildSymbolUrl(symbol, interval, startDate, endDate, limit);
        String[][] str2dArray = getRequestObjFromMarketApi(String[][].class, url);
        if(str2dArray == null) return new LinkedList<>();
        return ObjectMapper.mapToResponseDtoList(str2dArray);
    }


    @Override
    protected List<SimpleResponseDto> requestPricesForScheduler(Set<Symbol> symbols) {
        Set<String> set = symbols.stream()
                .map(e -> e.getCode())
                .collect(Collectors.toSet());
        return getPrices(set);
    }

    private String buildSymbolUrl(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        if(limit <=0 && limit >=1000) throw new IllegalArgumentException();

        long unixStartTime= DateTime.toUnixTime(startDate);
        long unixEndTime = DateTime.toUnixTime(endDate);

        StringBuilder url = new StringBuilder();
        url
                .append(URL_PRICE).append(symbol)
                .append(INTERVAL).append(interval.code)
                .append(START_TIME).append(unixStartTime)
                .append(END_TIME).append(unixEndTime)
                .append(LIMIT).append(limit);
        log.info(url.toString());
        return url.toString();
    }

    private String buildMultiSimpleSymbolsUrl(List<String> params){
        if(params == null) throw new IllegalArgumentException();

        StringBuilder url = new StringBuilder();
        url.append(URL_SIMPLE_PRICES);
        url.append("[");
        if(params.size()>1){
            for (String param : params) {
                url.append("\"").append(param).append("\"").append(",");
            }
            url.replace(url.lastIndexOf(","),url.lastIndexOf(",")+1,"");
        }else{
            url.append("\"").append(params.get(0)).append("\"");
        }
        url.append("]");
        //log.info(url.toString());
        return url.toString();
    }

    private <T> T getRequestObjFromMarketApi(Class<T> classType, String url) {
        return restTemplate.getForObject(url, classType);
    }

/*
    @Override
    public List<ResponseDto> getDetailPriceHistory(Symbol symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit){

        if(symbol == null) throw new IllegalArgumentException();
        String url = buildSymbolUrl(symbol, interval, startDate, endDate, limit);
        String[][] str2dArray = getRequestObjFromMarketApi(String[][].class, url);
        if(str2dArray == null) return new LinkedList<>();
        return ObjectMapper.mapToResponseDtoList(str2dArray);
    }

    @Override
    public List<SimpleResponseDto> getPrices(List<String> symbolsStr) {
        if(symbolsStr == null) return null;

        String url= buildMultiSimpleSymbolsUrl(symbolsStr);
        SimpleRequestDto[] objArray= getRequestObjFromMarketApi(SimpleRequestDto[].class, url);
        if(objArray == null) return new LinkedList<>();
        return ObjectMapper.mapToSimpleResponseDtoList(objArray);
    }*/

}
