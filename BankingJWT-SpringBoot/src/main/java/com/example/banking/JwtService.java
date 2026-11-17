package com.example.banking;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
@Service
public class JwtService {
 private final SecretKey key;
 public JwtService(@Value("${app.jwt.secret}") String secret) {
  key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
 }
 public String create(UserDetails user) {
  Instant now = Instant.now();
  return Jwts.builder().subject(user.getUsername()).issuedAt(Date.from(now))
   .expiration(Date.from(now.plusSeconds(1800))).signWith(key).compact();
 }
 public String username(String token) {
  return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
 }
}