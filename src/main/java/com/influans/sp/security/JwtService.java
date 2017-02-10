package com.influans.sp.security;

import com.google.common.collect.ImmutableMap;
import com.influans.sp.enums.UserRole;
import com.influans.sp.utils.DateUtils;
import com.influans.sp.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private static final String ISSUER = "ScrumPoker";

    private static final String SESSION_CLAIM = "sId";
    private static final String ROLE_CLAIM = "role";


    @Value("jwt.secret-key")
    private String secretKey;

    public String generate(String sessionId, String username, UserRole role) {
        final Date expiration = DateUtils.addHours(DateUtils.now(), 8);
        return buildToken(username, DateUtils.now(), expiration, ImmutableMap.of(SESSION_CLAIM, sessionId, ROLE_CLAIM, role));
    }

    private String buildToken(String id, Date now, Date expiration, Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder() //
                .signWith(SignatureAlgorithm.HS256, secretKey) //
                .setIssuer(ISSUER) //
                .setSubject(id) //
                .setId(UUID.randomUUID().toString()) //
                .setExpiration(expiration) //
                .setIssuedAt(now);
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            builder = builder.claim(entry.getKey(), entry.getValue());
        }
        return builder.compact();
    }


    public ScrumPokerAuthenticationToken authenticate(String token) {
        try {
            final Claims body = Jwts.parser().setSigningKey(secretKey).requireIssuer(ISSUER).parseClaimsJws(token).getBody();
            if (StringUtils.isEmpty(body.getSubject()) || !body.containsKey(SESSION_CLAIM) || !body.containsKey(ROLE_CLAIM)) {
                // Invalid token
                return null;
            }
            final String username = body.getSubject();
            final String sessionId = body.get(SESSION_CLAIM, String.class);
            final String role = body.get(ROLE_CLAIM, String.class);
            final Principal principal = new Principal(username, sessionId, UserRole.valueOf(role));
            return new ScrumPokerAuthenticationToken(principal);
        } catch (Exception e) {
            LOGGER.debug("Invalid JWT ", e);
            LOGGER.warn(e.getMessage());
            return null;
        }
    }

}
