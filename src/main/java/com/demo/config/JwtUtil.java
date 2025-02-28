package com.demo.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Slf4j
public class JwtUtil {

    private static final long EXPIRATION_TIME = 86400000;
    public static final String SECRET_KEY = "1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij";


    public static void secretKeyGenerator() {
        byte[] key = new byte[32]; // 256-bit key
        new SecureRandom().nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated SECRET_KEY: " + secretKey);
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            SignedJWT signedJWT =  SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getUsernameFromToken(String token) {
        JwtParser parser = Jwts.parser().verifyWith(getSigningKey()).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

//    public static String generateToken(Member member) {
//        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
//
//        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
//                .subject(member.getFull_name())
//                .issuer("dongnv.com")
//                .issueTime(new Date())
//                .expirationTime(new Date(
//                        Instant.now().plus(3600 , ChronoUnit.SECONDS).toEpochMilli()))
//                .jwtID(UUID.randomUUID().toString())
//                .claim("scope", buildScope(member))
//                .build();
//
//        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
//
//        JWSObject jwsObject = new JWSObject(header, payload);
//
//        try {
//            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
//            return jwsObject.serialize();
//        } catch (JOSEException e) {
//            log.error("Cannot create token", e);
//            throw new RuntimeException(e);
//        }
//    }

//    private static String buildScope(Member member) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//        stringJoiner.add("USER");
//        stringJoiner.add("ADMIN");
//        return stringJoiner.toString();
//    }




//    public static void main(String[] args) throws Exception {
////        JwtUtil.secretKeyGenerator();
//        String token = JwtUtil.generateToken("user@example.com");
//        System.out.println("Token: " + token);
//        System.out.println("Valid: " + JwtUtil.validateToken(token));
//    }
}
