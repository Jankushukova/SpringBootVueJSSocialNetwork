package com.okta.springbootvue.SpringBootVueApplication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static com.okta.springbootvue.SpringBootVueApplication.security.SecurityConstants.*;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   CustomUserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            com.okta.springbootvue.SpringBootVueApplication.models.User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), com.okta.springbootvue.SpringBootVueApplication.models.User.class);
            com.okta.springbootvue.SpringBootVueApplication.models.User user = userService.findByUsername(creds.getUsername());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Claims claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        System.out.println("successful authentication");
        String responseToClient = token;
        res.setStatus(HttpServletResponse.SC_OK);
        res.setHeader(HttpHeaders.CONTENT_TYPE, "text");
        res.getWriter().print(responseToClient);
        res.getWriter().flush();
        res.getWriter().close();
    }
}