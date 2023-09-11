package com.mj.market.app.user;

import com.mj.market.app.pricealert.PriceAlert;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name ="email")
    @Email(message = "Podaj prawidłowy adres e-mail")
    @NotBlank(message = "Podaj email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy= "user")
    private Set<PriceAlert> priceAlerts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
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
