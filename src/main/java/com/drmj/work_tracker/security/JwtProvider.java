package com.drmj.work_tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final ResourceLoader resourceLoader;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Value("${jwt.private-key-location}")
    private String privateKeyLocation;

    @Value("${jwt.public-key-location}")
    private String publicKeyLocation;

    @Value("${jwt.expiration-seconds}")
    private long expiration;

    @PostConstruct
    public void init() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");

        try (InputStream is = resourceLoader.getResource(privateKeyLocation).getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "").replaceAll("\\s", "");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
        }

        try (InputStream is = resourceLoader.getResource(publicKeyLocation).getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "").replaceAll("\\s", "");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
        }
    }

    public String generateBaseToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * 1000)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateToken(UUID userId, UUID organizationId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("organizationId", organizationId.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * 1000)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
    }
}