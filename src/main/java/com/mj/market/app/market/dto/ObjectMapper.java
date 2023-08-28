package com.mj.market.app.market.dto;

import com.mj.market.app.DateTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static List<SimpleResponseDto> valueOfSimpleResponseDtoList(SimpleRequestDto[] requestDtoArray) {
        List<SimpleResponseDto>responseDtoList = Arrays.asList(requestDtoArray)
                .stream()
                .filter(reqObj->reqObj != null)
                .map(reqObj-> new SimpleResponseDto(reqObj.getSymbol(),reqObj.getPrice()))
                .collect(Collectors.toList());
        return responseDtoList;
    }
}
