package com.example.banking;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class BankingController {
 private final AuthenticationManager manager; private final JwtService jwt;
 public BankingController(AuthenticationManager manager, JwtService jwt) { this.manager=manager; this.jwt=jwt; }

 @PostMapping("/api/auth/login")
 public ResponseEntity<?> login(@RequestBody LoginRequest body) {
  try {
   var auth=manager.authenticate(new UsernamePasswordAuthenticationToken(body.username(),body.password()));
   String token=jwt.create((UserDetails)auth.getPrincipal());
   return ResponseEntity.ok(Map.of("message","Login successful","accessToken",token,"tokenType","Bearer","expiresInMinutes",30));
  } catch (BadCredentialsException ex) {
   return ResponseEntity.status(401).body(Map.of("error","Invalid username or password"));
  }
 }
 @GetMapping("/api/banking/balance")
 public Map<String,Object> balance(Authentication auth) {
  return Map.of("accountHolder",auth.getName(),"accountNumber","XXXXXX1234","currency","INR","balance",50000.00);
 }
 @GetMapping("/api/banking/transactions")
 public Map<String,Object> transactions(Authentication auth) {
  return Map.of("accountHolder",auth.getName(),"transactions",List.of(
   Map.of("id","TXN001","type","DEPOSIT","amount",5000),
   Map.of("id","TXN002","type","WITHDRAWAL","amount",1000),
   Map.of("id","TXN003","type","TRANSFER","amount",2000)));
 }
 public record LoginRequest(String username,String password) {}
}