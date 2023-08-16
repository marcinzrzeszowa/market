package com.mj.market.market.dto;

import com.mj.market.market.DateTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectMapper {

    public static List<ResponseDto> mapToResponseDtoList(String[][] requestObjArray) {
        List<ResponseDto> result = Stream.of(requestObjArray)
                .map(reqObj->createSymbolDto(reqObj))
                .collect(Collectors.toList());
        return result;
    }

    private static ResponseDto createSymbolDto(String[] requestObj) {
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

    public static List<SimpleResponseDto> mapToSimpleResponseDtoList(SimpleRequestDto[] requestDtoArray) {
        List<SimpleResponseDto>responseDtoList = Arrays.asList(requestDtoArray)
                .stream()
                .filter(reqObj->reqObj != null)
                .map(reqObj-> new SimpleResponseDto(reqObj.getSymbol(),reqObj.getPrice()))
                .collect(Collectors.toList());
        return responseDtoList;
    }
}
