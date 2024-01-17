package com.mj.market.app.user;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.mj.market.app.pricealert.PriceAlert;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name="app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User implements UserDetails{

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

    @Column(name ="locked")
    private Boolean locked = false;

    @Column(name ="enabled")
    private Boolean enabled = false;

    @OneToMany(mappedBy= "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Set<PriceAlert> priceAlerts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(getRole().name()));
        return simpleGrantedAuthorityList;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
