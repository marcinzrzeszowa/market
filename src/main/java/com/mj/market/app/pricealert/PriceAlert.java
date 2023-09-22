package com.mj.market.app.pricealert;

import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Objects;

@Entity(name="PriceAlert")
@Table(name="price_alert")
public class PriceAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", length = 100)
    @NotBlank(message = "Podaj opis")
    private String description;

    @Column(name = "max_price", precision = 10, scale = 3)
    /*@Min(value=0, message="Cena maksymalna jest za mała")
    @Max(value=100000, message="Cena maksymalna jest za duża !!!!!!!!!!!!!!")*/
    private BigDecimal maxPrice;

    @Column(name = "min_price", precision = 10, scale = 3)
   /* @Min(value=0, message="Cena minimalna jest za mała")
    @Max(value=1000000, message="Cena minimalna jest za duża")*/
    private BigDecimal minPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "related_alert_id")
    private Long relatedAlertId;

    @Column(name = "communicate")
    private String communicate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "symbol")
    private Symbol symbol;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;


    @Transient
    private static final BigDecimal defaultMinPrice = BigDecimal.ZERO;
    @Transient
    private static final BigDecimal defaultMaxPrice = BigDecimal.ZERO;
    @Transient
    private static  final Long defaultNotifyAlertId = Long.valueOf(-1);
    @Transient
    private static final boolean defaultIsActive = true;
    @Transient
    private static final String defaultCommunicate = "no communicate" ;

    public PriceAlert() {
    }

    private PriceAlert(Symbol symbol, User user, String description, BigDecimal maxPrice, BigDecimal minPrice, Boolean isActive, Long relatedAlertId) {
        this.description = description;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.isActive = isActive;
        this.relatedAlertId = relatedAlertId;
        this.symbol = symbol;
        this.user = user;
        this.communicate = defaultCommunicate;
    }

    // Max and Min price
    public static PriceAlert newPriceAlertWithMaxMin(Symbol symbol, User user, String description, BigDecimal maxPrice, BigDecimal minPrice){
        return new PriceAlert(symbol,user, description, maxPrice, minPrice, defaultIsActive, defaultNotifyAlertId );
    }

    // just Max price
    public static PriceAlert newPriceAlertWithMax(Symbol symbol, User user, String description, BigDecimal maxPrice){
        return new PriceAlert(symbol,user, description, maxPrice, defaultMinPrice, defaultIsActive, defaultNotifyAlertId);
    }

    // just Min price
    public static PriceAlert newPriceAlertWithMin(Symbol symbol, User user, String description, BigDecimal minPrice){
        return new PriceAlert(symbol,user, description, defaultMaxPrice, minPrice, defaultIsActive, defaultNotifyAlertId);
    }

    //  Max and Min price with related price alert
    public static PriceAlert newPriceAlertWithRelation(Symbol symbol, User user, String description, BigDecimal maxPrice, BigDecimal minPrice,Long relatedAlertId){
        return new PriceAlert(symbol, user, description, maxPrice, minPrice, defaultIsActive, relatedAlertId);
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getRelatedAlertId() {
        return relatedAlertId;
    }

    public void setRelatedAlertId(Long relatedAlertId) {
        this.relatedAlertId = relatedAlertId;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommunicate() {
        return communicate;
    }

    public void setCommunicate(String communicate) {
        this.communicate = communicate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, maxPrice, minPrice, isActive, relatedAlertId, communicate, symbol, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceAlert)) return false;
        PriceAlert that = (PriceAlert) o;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "PriceAlert" +
                "{ "+
                "id=" + id +
                ", description='" + description + '\'' +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", isActive=" + isActive +
                ", symbol=" + symbol +
                " " + communicate +
                '}'+"\n";
    }
}
