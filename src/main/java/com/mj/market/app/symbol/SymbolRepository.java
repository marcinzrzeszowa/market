package com.mj.market.app.symbol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol,Long> {

    Symbol findByName(String name);

    Symbol findByCode(String code);

    List<Symbol> findByType(SymbolType tickerType);

    List<Symbol> findAll();

    Optional<Symbol> findById(Long id);

    Symbol save(Symbol entity);

    boolean existsById(Long id);

    void deleteById(Long id);

}
