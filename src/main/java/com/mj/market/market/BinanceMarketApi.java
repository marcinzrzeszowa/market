package com.mj.market.market;

import com.mj.market.market.dto.*;
import com.mj.market.market.main.MarketApiRequestSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class BinanceMarketApi extends MarketApiRequestSequence implements CryptoMarketApi {

    private RestTemplate restTemplate = new RestTemplate();
    private final List<BinanceSymbol> symbols = Arrays.asList(BinanceSymbol.values());

    //API URLs
    private static final String URL_API = "https://api.binance.com/api/v3";
    private static final String URL_SIMPLE_PRICES= URL_API + "/ticker/price?symbols=";
    private static final String URL_PRICE =  URL_API + "/klines?symbol=";
    private final static String INTERVAL = "&interval=";
    private final static String LIMIT = "&limit=";
    private final static String START_TIME = "&startTime=";
    private final static String END_TIME = "&endTime=";


    @Override
    public List<SimpleResponseDto> getAllPrices(){
        return getPrices(symbols);
    }

    @Override
    public List<ResponseDto> getDetailPriceHistory(BinanceSymbol symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit){

        if(symbol == null) throw new IllegalArgumentException();
        String url = buildSymbolUrl(symbol, interval, startDate, endDate, limit);
        String[][] str2dArray = getRequestObjFromMarketApi(String[][].class, url);
        if(str2dArray == null) return new LinkedList<>();
        return ObjectMapper.mapToResponseDtoList(str2dArray);
    }

    @Override
    public List<SimpleResponseDto> getPrices(List<BinanceSymbol> selectedSymbols) {
        String url= buildMultiSimpleSymbolsUrl(selectedSymbols);
        SimpleRequestDto[] objArray= getRequestObjFromMarketApi(SimpleRequestDto[].class, url);
        if(objArray == null) return new LinkedList<>();
        return ObjectMapper.mapToSimpleResponseDtoList(objArray);
    }

    @Override
    protected List<SimpleResponseDto> requestPrices(Set<String> marketSymbols) {
        List<BinanceSymbol> symbols= getSymbols(marketSymbols);
        return getPrices(symbols);
    }

    private static List<BinanceSymbol> getSymbols(Set<String> marketSymbols) {
        List<BinanceSymbol> symbols = new LinkedList<>();
        for(String bs: marketSymbols){
            if(BinanceSymbol.findSymbol(bs) != null) symbols.add(BinanceSymbol.findSymbol(bs));
        }
        return  symbols;
    }

    private String buildSymbolUrl(BinanceSymbol symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit) {
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

    private String buildMultiSimpleSymbolsUrl(List<BinanceSymbol> params){
        if(params == null) throw new IllegalArgumentException();

        StringBuilder url = new StringBuilder();
        url.append(URL_SIMPLE_PRICES);
        url.append("[");
        if(params.size()>1){
            for (BinanceSymbol param : params) {
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


}
