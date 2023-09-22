package com.mj.market.app.market.api;

import com.mj.market.config.DateTime;
import com.mj.market.app.notifier.EmailService;
import com.mj.market.app.market.MarketSchedulerSequence;
import com.mj.market.app.market.dto.ObjectMapper;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleRequestDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Qualifier("BinanceMarketApi")
public class BinanceMarketApi extends MarketSchedulerSequence implements MarketApi{
    private RestTemplate restTemplate = new RestTemplate();
    private static final EnumSet<SymbolType> handledSymbols = EnumSet.of(SymbolType.KRYPTOWALUTA);

    //API URLs
    private static final String URL_API = "https://api.binance.com/api/v3";
    private static final String URL_SIMPLE_PRICES= URL_API + "/ticker/price?symbols=";
    private static final String URL_PRICE =  URL_API + "/klines?symbol=";
    private final static String INTERVAL = "&interval=";
    private final static String LIMIT = "&limit=";
    private final static String START_TIME = "&startTime=";
    private final static String END_TIME = "&endTime=";

    public BinanceMarketApi(PriceAlertService priceAlertService, SymbolService symbolService, EmailService emailService) {
        super("BinanceMarketApi", priceAlertService, symbolService, emailService);
    }

    @Override
    public List<SimpleResponseDto> getAllPrices(){
        Set<String> strSymbols = super.getAllSymbolsSupportedByMarketApi();
        SimpleRequestDto[] requestDto = getPrices(strSymbols);
        List<SimpleResponseDto> response = ObjectMapper.valueOfSimpleResponseDtoList(requestDto);
        return response;
    }

    @Override
    public SimpleRequestDto[] getPrices(Set<String> filteredSymbols) {
        List<String> filteredSymbolsList = filteredSymbols.stream().toList();
        String url= buildMultiSimpleSymbolsUrl(filteredSymbolsList);
        SimpleRequestDto[] objArray= getRequestObjFromMarketApi(SimpleRequestDto[].class, url);
        if(objArray == null) return new SimpleRequestDto[0];
        return objArray;
    }

    //TODO add to website in details
    @Override
    public List<ResponseDto> getDetailPriceHistory(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit){
        boolean isValidSymbol = super.checkIfSymbolIsValid(symbol);
        if(symbol == null || !isValidSymbol) throw new IllegalArgumentException();

        String url = buildSymbolUrl(symbol, interval, startDate, endDate, limit);
        String[][] str2dArray = getRequestObjFromMarketApi(String[][].class, url);
        if(str2dArray == null) return new LinkedList<>();
        return ObjectMapper.valueOfResponseDtoList(str2dArray);
    }

    @Override
    protected List<SimpleResponseDto> requestPricesForScheduler(Set<Symbol> filteredSymbols) {
        Set<String> symbolsStr = filteredSymbols.stream()
                .map(e -> e.getCode())
                .collect(Collectors.toSet());
        SimpleRequestDto[] requestDto = getPrices(symbolsStr);
        List<SimpleResponseDto> response = ObjectMapper.valueOfSimpleResponseDtoList(requestDto);
        return response;
    }

    @Override
    protected Set<SymbolType> setSupportedSymbolType() {
        return handledSymbols;
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
        return url.toString();
    }

    private String buildMultiSimpleSymbolsUrl(List<String> symbols){
        if(symbols == null || symbols.isEmpty()) throw new IllegalArgumentException("buildMultiSimpleSymbolsUrl(): No Symbols in list");

        StringBuilder url = new StringBuilder();
        url.append(URL_SIMPLE_PRICES);
        url.append("[");
        if(symbols.size()>1){
            for (String param : symbols) {
                url.append("\"").append(param).append("\"").append(",");
            }
            url.replace(url.lastIndexOf(","),url.lastIndexOf(",")+1,"");
        }else{
            url.append("\"").append(symbols.get(0)).append("\"");
        }
        url.append("]");
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
