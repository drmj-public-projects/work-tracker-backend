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

    @Value("${jwt.private-key-location:}")
    private String privateKeyLocation;

    @Value("${jwt.public-key-location:}")
    private String publicKeyLocation;

    @Value("${jwt.private-key-base64:}")
    private String privateKeyBase64;

    @Value("${jwt.public-key-base64:}")
    private String publicKeyBase64;

    @Value("${jwt.expiration-seconds}")
    private long expiration;

    @PostConstruct
    public void init() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");

        // Validate that at least one key source is configured
        boolean hasPrivateKeySource = isNotBlank(privateKeyBase64) || isNotBlank(privateKeyLocation);
        boolean hasPublicKeySource = isNotBlank(publicKeyBase64) || isNotBlank(publicKeyLocation);

        if (!hasPrivateKeySource) {
            throw new IllegalStateException(
                "JWT private key not configured. Set either JWT_PRIVATE_KEY_BASE64 (env var) or jwt.private-key-location property.");
        }
        if (!hasPublicKeySource) {
            throw new IllegalStateException(
                "JWT public key not configured. Set either JWT_PUBLIC_KEY_BASE64 (env var) or jwt.public-key-location property.");
        }

        if (isNotBlank(privateKeyBase64)) {
            this.privateKey = loadPrivateKeyFromBase64(privateKeyBase64, kf);
        } else {
            this.privateKey = loadPrivateKeyFromFile(privateKeyLocation, kf);
        }

        if (isNotBlank(publicKeyBase64)) {
            this.publicKey = loadPublicKeyFromBase64(publicKeyBase64, kf);
        } else {
            this.publicKey = loadPublicKeyFromFile(publicKeyLocation, kf);
        }
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private RSAPrivateKey loadPrivateKeyFromBase64(String base64, KeyFactory kf) throws Exception {
        // Step 1: Decode the outer Base64 to recover the PEM text
        String pem = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        // Step 2: Strip PEM headers and whitespace (same as file mode)
        String key = pem.replaceAll("-----\\w+ PRIVATE KEY-----", "").replaceAll("\\s", "");
        // Step 3: Decode the inner Base64 (the actual key bytes)
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    private RSAPrivateKey loadPrivateKeyFromFile(String location, KeyFactory kf) throws Exception {
        try (InputStream is = resourceLoader.getResource(location).getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "").replaceAll("\\s", "");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            return (RSAPrivateKey) kf.generatePrivate(keySpec);
        }
    }

    private RSAPublicKey loadPublicKeyFromBase64(String base64, KeyFactory kf) throws Exception {
        // Step 1: Decode the outer Base64 to recover the PEM text
        String pem = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        // Step 2: Strip PEM headers and whitespace (same as file mode)
        String key = pem.replaceAll("-----\\w+ PUBLIC KEY-----", "").replaceAll("\\s", "");
        // Step 3: Decode the inner Base64 (the actual key bytes)
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
        return (RSAPublicKey) kf.generatePublic(keySpec);
    }

    private RSAPublicKey loadPublicKeyFromFile(String location, KeyFactory kf) throws Exception {
        try (InputStream is = resourceLoader.getResource(location).getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "").replaceAll("\\s", "");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            return (RSAPublicKey) kf.generatePublic(keySpec);
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