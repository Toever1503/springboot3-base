package com.springboot3base.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider { // JWT provider

    @Value("spring.jwt.secret")
    private String secretKey;
    private final long defaultExpirationTime; // token valid for 1 day
    private final long defaultRefreshTokenExpirationTime; // refresh token valid for 30days
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.defaultExpirationTime = 1000L * 60 * 60 * 24;
        this.defaultRefreshTokenExpirationTime = 1000L * 60 * 60 * 24 * 30;
    }

    @PostConstruct
    protected void init() {
        secretKey = Keys.class.getName() + SignatureAlgorithm.HS512
                + Base64.getEncoder().encodeToString(secretKey.getBytes());

    }

    // Jwt token creating func
    public String createToken(String userId, List<String> roles, Boolean isRefreshToken, Long expirationTime, StringBuilder expireDate) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);
        Date now = new Date();
        if (expirationTime != null && expirationTime <= 60000) {
            log.warn("Given expiration time is quite short (less than 1 min)!");
            log.warn("Set expiration time to default(1 day)");
            expirationTime = defaultExpirationTime;
        }
        Date expireTime = new Date(now.getTime() + (expirationTime == null ? (isRefreshToken ? defaultRefreshTokenExpirationTime : defaultExpirationTime) : expirationTime));
        expireDate.append(expirationTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(this.secretKey.getBytes()))).compact();
    }


    // Get authentication from token
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Get user id from token
    public String getUserIdFromToken(String token) {
        return getJwtParser()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean hasRole(String token, String input) {
        List<?> roles = (List<?>) getJwtParser().parseClaimsJws(token).getBody().get("roles");
        return roles.stream().allMatch(role -> role.equals(input));
    }

    // Get token in header(x-api-token) from req
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("x-api-token");
    }

    // Check token is still valid
    public boolean validateToken(String jwtToken) {
        try {
            Date now = new Date();
            Jws<Claims> claims = getJwtParser().parseClaimsJws(jwtToken);

            Date expire = claims.getBody().getExpiration();
            if (expire.before(now)) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                log.error("token expire : expire({})/now({})", dateFormat.format(expire), dateFormat.format(now));
            }
            return !expire.before(now);
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token. : {}({})", jwtToken, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("Token has expired. : {}({})", jwtToken, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Token isn't JWT type. : {}({})", jwtToken, e.getMessage());
        } catch (Exception e) {
            log.error("Unknown JWT exception : {}({})", jwtToken, e.getMessage());
        }
        return false;
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()))).build();
    }
}