package com.mj.market.app.market.dto;

import com.mj.market.app.symbol.Symbol;
import com.mj.market.config.DateTime;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ObjectMapper {

    public static List<ResponseDto> valueOfResponseDtoList(String[][] requestObjArray) {
        List<ResponseDto> result = Stream.of(requestObjArray)
                .map(reqObj-> valueOfSymbolDto(reqObj))
                .collect(Collectors.toList());
        return result;
    }

    private static ResponseDto valueOfSymbolDto(String[] requestObj) {
        return new ResponseDto(
                DateTime.toLocalDateTime(Long.valueOf(requestObj[0])),
                new BigDecimal(requestObj[1]),
                new BigDecimal(requestObj[2]),
                new BigDecimal(requestObj[3]),
                new BigDecimal(requestObj[4]),
                new BigDecimal(requestObj[5]),
                DateTime.toLocalDateTime(Long.valueOf(requestObj[6])),
                null
        );
    }

    public static List<SimpleResponseDto> valueOfSimpleResponseDTOList(SimpleRequestDto[] requestDtoArray, Set<Symbol> symbols) {
        if(requestDtoArray == null || requestDtoArray.length == 0 || symbols.isEmpty())throw new IllegalArgumentException("No array to build SimpleResponseDto List");
        String name="";
        List<SimpleResponseDto>responseDtoList = Arrays.asList(requestDtoArray)
                .stream()
                .filter(obj->obj != null)
                .map(obj-> new SimpleResponseDto(
                        addNameFromSymbol(obj.getSymbol(), symbols),
                        obj.getSymbol(),
                        formatPrice(obj.getPrice()))
                )
                .collect(Collectors.toList());
        return responseDtoList;
    }

    public static SimpleResponseDto valueOfSimpleResponseDTO(SimpleRequestDto requestObj, String name) {
        if(requestObj == null || name.isEmpty())throw new IllegalArgumentException("Wrong arguments to build SimpleResponseDto");
        return new SimpleResponseDto(name, requestObj.getSymbol(), formatPrice(requestObj.getPrice()));
    }

    private static BigDecimal formatPrice(BigDecimal price) {
       return price.setScale(3, RoundingMode.HALF_UP);
    }

    private static String addNameFromSymbol(String code, Set<Symbol> symbols){
        String defaultExceptionName = " - ";
        return symbols.stream()
                .filter(e->e.getCode().equals(code))
                .map(e->e.getName())
                .findAny()
                .orElse(" - ");
    }

}
