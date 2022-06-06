package com.stibits.rnft.commonweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.stibits.rnft.commonweb.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {
    protected HttpSecurity commonConfig(HttpSecurity http) throws Exception {
        return http.csrf().disable()
            .logout().disable()
            .httpBasic().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter () {
        return new JwtAuthenticationFilter();
    }
}
