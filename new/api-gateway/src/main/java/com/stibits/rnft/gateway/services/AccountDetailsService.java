package com.stibits.rnft.gateway.services;

import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.AccountDetails;
import com.stibits.rnft.gateway.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AccountDetailsService implements ReactiveUserDetailsService {
    private static final String ERROR_MESSAGE = "Account with this username does not exist.";
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Account account = accountRepository.getAccountByUsername(username);
        
        if (account == null) throw new UsernameNotFoundException(ERROR_MESSAGE);

        return Mono.just(new AccountDetails(account));
    }
}
