package com.mj.market.app.symbol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SymbolService {

    private final SymbolRepository symbolRepository;
    private static Set<String> validSymbolFormats;

    @Autowired
    public SymbolService(SymbolRepository stockTickerRepository) {
        this.symbolRepository = stockTickerRepository;
        validSymbolFormats = defineValidSymbolsFormats();
    }

    private Set<String> defineValidSymbolsFormats() {
        List<Symbol> symbols = getAllSymbols();
        Set<String> codes = symbols.stream()
                .map( symbol -> symbol.getCode())
                .collect(Collectors.toSet());
        return codes;
    }

    public Symbol findById(Long id){
        return symbolRepository.findById(id).get();
    }

    public List<Symbol> getAllSymbols(){
        return symbolRepository.findAll();
    }

    public static Set<String> getAllSymbolFormats() {
        return validSymbolFormats;
    }

    public List<String> getSymbolsByCode(Set<String> symbolsCodes) {
        Set<String> set = new HashSet<>();
        for (String code: symbolsCodes){
             if(validSymbolFormats.contains(code))set.add(code);
        }
        return set.stream().toList();
    }

    public String getSymbolsByCode(String code) {
        String result = validSymbolFormats.contains(code)? code: "";
        return result;
    }
}
