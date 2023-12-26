package com.example.demo.controller;

import com.example.demo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Integer expiredTimeMs;

    @RequestMapping("/create")
    public ResponseEntity create() {


        return ResponseEntity.ok().body(JwtUtils.generateAccessToken("test01", secretKey, expiredTimeMs));
    }

    @RequestMapping("/valid")
    public ResponseEntity valid(String username, String token) {

        return ResponseEntity.ok().body(JwtUtils.validate(token, username, secretKey));
    }
}
