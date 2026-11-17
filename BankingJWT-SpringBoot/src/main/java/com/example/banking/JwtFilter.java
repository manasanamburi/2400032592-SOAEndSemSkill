package com.example.banking;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UserDetailsService users;
 public JwtFilter(JwtService jwt, UserDetailsService users) { this.jwt=jwt; this.users=users; }
 @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
  String header=req.getHeader("Authorization");
  if(header!=null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null) {
   try {
    String name=jwt.username(header.substring(7));
    UserDetails user=users.loadUserByUsername(name);
    var auth=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(auth);
   } catch (Exception ex) { SecurityContextHolder.clearContext(); }
  }
  chain.doFilter(req,res);
 }
}