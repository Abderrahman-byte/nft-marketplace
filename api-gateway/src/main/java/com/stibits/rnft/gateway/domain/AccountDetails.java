package com.stibits.rnft.gateway.domain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class AccountDetails implements UserDetails {
    @Getter @Setter private Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return account != null ? account.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return account != null ? account.getUsername() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return account != null ? account.isActive() : false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account != null ? account.isActive() : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account != null ? account.isActive() : false;
    }

    @Override
    public boolean isEnabled() {
        return account != null ? account.isActive() : false;
    }
    
}
