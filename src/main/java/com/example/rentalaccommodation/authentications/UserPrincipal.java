package com.example.rentalaccommodation.authentications;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//@Getter
//@Setter
@Data
public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String token;
    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String name, Collection<? extends GrantedAuthority> authorities, String email) {
        super();
        this.id = id;
        this.name = name;
        this.authorities = authorities;
        this.email = email;
    }


    public UserPrincipal() {
    }


    public static UserPrincipal create(TokenUser tokenUser) {
        List<GrantedAuthority> authorities = tokenUser.getAuthorities().stream().map((scope) -> new SimpleGrantedAuthority(scope)).collect(Collectors.toList());

        return new UserPrincipal(
                tokenUser.getId(),
                tokenUser.getName(),
                authorities,
                tokenUser.getEmail()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
