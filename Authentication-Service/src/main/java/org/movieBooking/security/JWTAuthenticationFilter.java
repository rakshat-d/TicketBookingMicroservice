package org.movieBooking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.movieBooking.config.SecurityConstants;
import org.movieBooking.config.Utilities;
import org.movieBooking.entities.ConnValidationResponse;
import org.movieBooking.entities.JwtAuthenticationModel;
import org.movieBooking.entities.TokensEntity;
import org.movieBooking.services.TokensRedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private ObjectMapper mapper=new ObjectMapper();

    private final TokensRedisService tokensRedisService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JwtAuthenticationModel authModel = mapper.readValue(request.getInputStream(), JwtAuthenticationModel.class);
            log.info("auth_model: " + authModel);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword());
            var auth = authenticationManager.authenticate(authentication);
            log.info("auth:  " +  auth);
            return auth;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    	var key = Keys.hmacShaKeyFor(SecurityConstants.KEY.getBytes());

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .claim("principal", authResult.getPrincipal())
                .setIssuedAt(new Date())
                .setIssuer(SecurityConstants.ISSUER)
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info(token);
        TokensEntity tokensEntity = TokensEntity.builder().id(Utilities.generateUuid()).authenticationToken(token)
                        .username(authResult.getName())
                        .createdBy("SYSTEM").createdOn(LocalDateTime.now())
                        .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
                        .build();
        tokensEntity = tokensRedisService.save(tokensEntity);
        String bearerToken = String.format("Bearer %s", tokensEntity.getId());
        response.addHeader("Authorization", bearerToken);
//        response.addHeader("Expiration", String.valueOf(30*60));

        var authorities = authResult.getAuthorities().stream().map(o -> (GrantedAuthority) o).collect(Collectors.toList());

        ConnValidationResponse respModel = ConnValidationResponse.builder()
                .isAuthenticated(true)
                .token(bearerToken)
                .methodType(request.getMethod())
                .username(authResult.getName())
                .authorities(authorities)
                .build();
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(respModel));
    }
}