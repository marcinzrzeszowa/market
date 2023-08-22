package com.mj.market.app.user;

import com.mj.market.app.pricealert.PriceAlert;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;


@Entity(name ="User")
@Table(name="app_user")
public class User implements UserDetails {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="username")
    @NotBlank(message = "Podaj nazwe")
    private String username;

    @Column(name ="password")
    @NotBlank(message = "Podaj hasło")
    //@Size(min = 3, max = 50, message = "Długość hasła od 3 do 50 znaków")
    private String password;

    @Column(name ="role")
    private String role;

    @Column(name ="email")
    @Email(message = "Podaj prawidłowy adres e-mail")
    @NotBlank(message = "Podaj email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy= "user")
    private Set<PriceAlert> priceAlerts;

    public User() {
    }

    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PriceAlert> getPriceAlerts() {
        return priceAlerts;
    }

    public void setPriceAlerts(Set<PriceAlert> priceAlerts) {
        this.priceAlerts = priceAlerts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role)); //1 rola
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
