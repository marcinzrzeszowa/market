package com.mj.market.market.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "symbol")
public class Symbol {

    @Id
    private long id;
    @Column(name = "symbol")
    private String symbol;

}
