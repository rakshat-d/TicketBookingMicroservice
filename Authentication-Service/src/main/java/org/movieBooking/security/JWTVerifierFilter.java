package org.movieBooking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.movieBooking.config.SecurityConstants;
import org.movieBooking.config.Utilities;
import org.movieBooking.entities.TokensEntity;
import org.movieBooking.entities.User;
import org.movieBooking.services.TokensRedisService;
import org.movieBooking.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JWTVerifierFilter extends OncePerRequestFilter {

    private final TokensRedisService tokensRedisService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if(!(Utilities.validString(bearerToken) && bearerToken.startsWith(SecurityConstants.PREFIX))) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String authToken = bearerToken.replace(SecurityConstants.PREFIX, "");
        log.info("auth token : " + authToken);

        Optional<TokensEntity> tokensEntity = tokensRedisService.findById(authToken);

        if(tokensEntity.isEmpty()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = tokensEntity.get().getAuthenticationToken();
        log.info("token : " + token);
        var key = Keys.hmacShaKeyFor(SecurityConstants.KEY.getBytes());
        Jws<Claims> authClaim = Jwts.parserBuilder().setSigningKey(key)
                .requireIssuer(SecurityConstants.ISSUER)
                .build().parseClaimsJws(token);

        String username = authClaim.getBody().getSubject();

        log.info("username : " + username);

        List<Map<String, String>> authorities = (List<Map<String, String>>) authClaim.getBody().get("authorities");
        List<GrantedAuthority> grantedAuthorities = authorities.stream().map(map -> new SimpleGrantedAuthority(map.get("authority")))
                .collect(Collectors.toList());
        log.info(grantedAuthorities.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        log.info("authentication " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getByUsername(username).get();

        log.info("User : " + user);

        httpServletRequest.setAttribute("username", username);
        httpServletRequest.setAttribute("userId", user.getId());
        httpServletRequest.setAttribute("authorities", grantedAuthorities);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}