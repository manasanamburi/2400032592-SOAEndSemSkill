package com.example.banking;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder encoder() { return new BCryptPasswordEncoder(); }
 @Bean UserDetailsService users(PasswordEncoder e) {
  return new InMemoryUserDetailsManager(User.withUsername("manasa").password(e.encode("Bank@12345")).roles("CUSTOMER").build());
 }
 @Bean AuthenticationManager manager(AuthenticationConfiguration c) throws Exception { return c.getAuthenticationManager(); }
 @Bean SecurityFilterChain chain(HttpSecurity http, JwtFilter filter) throws Exception {
  return http.csrf(c->c.disable()).httpBasic(c->c.disable()).formLogin(c->c.disable())
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/api/auth/login").permitAll().anyRequest().authenticated())
   .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
   .exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->{
    res.setStatus(401); res.setContentType("application/json");
    res.getWriter().write("{\"error\":\"Unauthorized - provide a valid Bearer token\"}");
   })).build();
 }
}