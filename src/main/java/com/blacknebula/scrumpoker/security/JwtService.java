package com.blacknebula.scrumpoker.security;

import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.service.UserService;
import com.blacknebula.scrumpoker.utils.DateUtils;
import com.blacknebula.scrumpoker.utils.JsonSerializer;
import com.blacknebula.scrumpoker.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private static final String ISSUER = "ScrumPoker";

    private static final String SESSION_CLAIM = "sId";
    private static final String ROLE_CLAIM = "role";


    @Autowired
    private UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration:5}")
    private Integer expirationAfter;

    /**
     * @param sessionId session id
     * @param username  username
     * @param role      UserRole
     * @return jwt token
     * @should generate a valid jwt token
     */
    public String generate(String sessionId, String username, UserRole role) {
        final Date expiration = DateUtils.addSeconds(DateUtils.now(), expirationAfter);
        return buildToken(username, DateUtils.now(), expiration, ImmutableMap.of(SESSION_CLAIM, sessionId, ROLE_CLAIM, role));
    }

    /**
     * @param subject        token subject
     * @param creationTime   token creation time
     * @param expirationTime token expiration time
     * @param claims         token claims
     * @return jwt token
     */
    private String buildToken(String subject, Date creationTime, Date expirationTime, Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder() //
                .signWith(SignatureAlgorithm.HS256, secretKey) //
                .setIssuer(ISSUER) //
                .setSubject(subject) //
                .setId(UUID.randomUUID().toString()) //
                .setExpiration(expirationTime) //
                .setIssuedAt(creationTime);
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            builder = builder.claim(entry.getKey(), entry.getValue());
        }
        return builder.compact();
    }

    /**
     * @param token jwt token
     * @return ScrumPokerAuthenticationToken
     * @should parse token claims if token is valid
     * @should disconnect user if token has expired
     */
    ScrumPokerAuthenticationToken authenticate(String token) {
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
            LOGGER.warn("Invalid JWT ", e);
            if (e instanceof ExpiredJwtException) {
                final JwtPayload jwtPayload = parse(token);
                userService.disconnectUserInternal(jwtPayload.getSessionId(), jwtPayload.getUsername());
            }
            return null;
        }
    }

    /**
     * @param token token
     * @return parts
     * @should retrieve expired token content
     */
    public JwtPayload parse(String token) {
        final String encodedPayload = token.split("\\.")[1]
                .replace('-', '+').replace('_', '/');
        final String decoded = new String(Base64.getDecoder().decode(encodedPayload));
        return JsonSerializer.toObject(decoded, JwtPayload.class);
    }

}
