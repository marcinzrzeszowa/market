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
    private static Set<Symbol> validSymbolFormats;

    @Autowired
    public SymbolService(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
        createValidSymbolsFormats();
    }

    private void createValidSymbolsFormats() {
        List<Symbol> symbols = getAllSymbols();
        this.validSymbolFormats = symbols.stream().collect(Collectors.toSet());
    }

    public Symbol findById(Long id){
        return symbolRepository.findById(id).get();
    }

    public List<Symbol> getAllSymbols(){
        return symbolRepository.findAll();
    }

    public static Set<Symbol> getAllSymbolFormats() {
        return validSymbolFormats;
    }

    public List<String> getSymbolsInCorrectFormat(Set<String> symbolsCodes) {
        return symbolsCodes.stream().toList();
    }

    public String getSymbolsInCorrectFormat(String code) {
        String result = validSymbolFormats.contains(code)? code: "";
        return result;
    }

    public void saveAllSymbols(List<Symbol> list) {
        symbolRepository.saveAll(list);
        refreshValidSymbolFormats();
    }

    private void refreshValidSymbolFormats() {
        createValidSymbolsFormats();
    }
}
