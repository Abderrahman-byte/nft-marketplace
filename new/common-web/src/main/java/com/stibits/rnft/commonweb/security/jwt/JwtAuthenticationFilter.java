package com.stibits.rnft.commonweb.security.jwt;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stibits.rnft.common.api.ProfileDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.shared.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    private JwtParser jwtParser;


    @PostConstruct
    public void generateSecretKey () {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder()
            .deserializeJsonWith(new JacksonDeserializer<>(Map.of("details", ProfileDetails.class, "sub", String.class)))
            .setSigningKey(this.secretKey).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            this.getJwtAuthentication(request, response, filterChain);
        } catch (Exception ex) {
            if (ex instanceof ServletException) throw (ServletException)ex;
            if (ex instanceof IOException) throw (IOException)ex;
            
            log.error("[" + ex.getClass() + "] " + ex.getMessage(), ex);
            filterChain.doFilter(request, response);
        }
    }

    private void getJwtAuthentication (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception {
        String token = this.getRequestToken(request);

        if (!this.validateToken(token)) {
            filterChain.doFilter(request, response); 
            return;
        }

        Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
        ProfileDetails profileDetails = claims.get("details", ProfileDetails.class);
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) context = new SecurityContextImpl();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(profileDetails, token, AuthorityUtils.NO_AUTHORITIES));

        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    private String getRequestToken (HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION); 

        if (!StringUtils.hasText(bearer) || !bearer.startsWith(BEARER_PREFIX)) return null;

        return bearer.substring(BEARER_PREFIX.length());
    }

    private boolean validateToken (String token) {
        if (!StringUtils.hasText(token)) return false;

        try {
            this.jwtParser.parse(token);
            return true;
        } catch (JwtException ex) {
            log.info("[JwtException] invalid jwt : " + ex.getMessage());
            return false;
        }
    }
    
}
