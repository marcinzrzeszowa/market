package com.mj.market.app.symbol;


import com.mj.market.app.pricealert.PriceAlert;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity(name="Symbol")
@Table(name="symbol")
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @NotBlank(message = "Podaj kod")
    private String code;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SymbolType type;

    @Column(name = "price_alerts")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy ="symbol")
    private Set<PriceAlert> priceAlerts;

    public Symbol() {
    }

    public Symbol(String name, String code, SymbolType type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public Set<PriceAlert> getPriceAlerts() {
        return priceAlerts;
    }

    public void setPriceAlerts(Set<PriceAlert> priceAlerts) {
        this.priceAlerts = priceAlerts;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol that = (Symbol) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode() +21;
    }

    @Override
    public String toString() {
        return code;
    }
}
