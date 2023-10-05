package com.mj.market.app.symbol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymbolService {
    private final SymbolRepository symbolRepository;
    private static List<Symbol> validSymbolFormats;

    @Autowired
    public SymbolService(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
        createValidSymbolsFormats();
    }

    private void createValidSymbolsFormats() {
        this.validSymbolFormats = getAllSymbols();
    }

    public void saveAllSymbols(List<Symbol> list) {
        symbolRepository.saveAll(list);
        refreshValidSymbolFormats();
    }

    private void refreshValidSymbolFormats() {
        createValidSymbolsFormats();
    }

    public Symbol getSymbolByCode(String code){
        Symbol result = validSymbolFormats.stream()
                .filter(e->e.getCode().equals(code))
                .findAny().get();
        return result;
    }

    public List<Symbol> getAllSymbols(){
        return symbolRepository.findAll();
    }

    public boolean isSymbolByFormat(String code) {
        return (getSymbolByCode(code) != null)? true : false;
    }


}
