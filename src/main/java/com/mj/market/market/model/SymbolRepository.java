package com.mj.market.market.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol,Long> {


    @Query("SELECT s FROM Symbol s where symbol= ?1")
    Symbol findSymbolBySymbol(String param1);

    @Query("SELECT s FROM Symbol s where symbol= :symbol")
    Symbol findSymbolBySymbol2(@Param("symbol") String param1);

    // Query dla join z one to many
    //@Query("SELECT s FROM Symbol s left join fetch s.comment ")
    List<Symbol>findAll();

}
