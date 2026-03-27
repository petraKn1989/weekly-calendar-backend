package com.example.demo.security;

import com.example.demo.entity.User; 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    System.out.println("DEBUG: Načítám autority pro uživatele: " + user.getUsername());
    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
        new SimpleGrantedAuthority("ROLE_USER"),
        new SimpleGrantedAuthority("USER")
    );
    System.out.println("DEBUG: Předávám tyto role: " + authorities);
    return authorities;
}

    @Override
public String getPassword() {
    System.out.println("DEBUG: Heslo z databáze pro " + user.getUsername() + " je: " + user.getPassword());
    return user.getPassword();
}

    @Override
    public String getUsername() {
        return user.getUsername(); // Vrací jméno z tvé DB
    }

    // Všechny tyto metody musí vracet true, aby se uživatel mohl přihlásit
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public User getUser() {
        return user;
    }
}
