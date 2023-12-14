package com.dingli.springsecuritypersonal;

import com.dingli.springsecuritypersonal.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Key;
import java.util.Date;

@SpringBootTest
class SpringSecurityPersonalApplicationTests {

//    @Autowired
//    private JWTProvider jwtProvider;

    @Test
    void contextLoads() {
        User user = new User();
        user.setId(1L);
        user.setLogin("user");
        user.setPassword("user");
        user.setRole("ROLE_USER");
        String secretKey = "MDk5ZmU2YzdhZWE5NWRhZTU0MjgzMTVmMTkxYTI5ZGJmODc3NWU2ZDc5OWI1YWMxZTE5NWYxZWVhY" +
                "2VmZGYwMWQ1NmExNjI4M2M2OWUzOGM0Nzg1ZGU2YzgxNWVjYzNhODg4YzE0ODhlZDA0YjZlYTgzYzk3MGE4NWFkMmJmOGI=";
        secretKey = "eyJzdWIiOiJ1c2VyIiwidXNlciI6eyJpZCI6MSwibG9naW4iOiJ1c2VyIiwicGFzc3dvcmQiOiIkMmEkMTAkU1VxM3cvYnp1cFpWZExlSTFEWmlKdWxQMXBGOUdyQWxjVkRHWXNXd1VQUXVOYjBHVGY0OXEiLCJyb2xlIjoiUk9MRV9VU0VSIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiYWNjb3VudE5vbkxvY2tlZCI6ZmFsc2UsImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJjcmVkZW50aWFsc05vbkV4cGlyZWQiOnRydWUsInVzZXJuYW1lIjoidXNlciJ9LCJleHAiOjE2NTE1OTEwMDl9";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
        System.out.println(jws);
        String compact = Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(key,SignatureAlgorithm.HS512)
                .setExpiration(new Date(new Date().getTime() + 10000))
                .compact();
        System.out.println(compact);

        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(compact).getBody();
        System.out.println(body);
        String subject = body.getSubject();
        System.out.println(subject);
    }

    @Test
    void parseJWT() {
        String s = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjA2ODA2OTA3fQ.xKJHVH7O6MtRFlj5W1CPewRaM9MlQwQOr3Yy1go_y4fzD65YfPfkG2abw1OW0-OVF54nGzNOJ9Af9bQBo5Q03Q";
    }

    @Test
    void encode() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("user");
        String password2 = bCryptPasswordEncoder.encode("admin");
        System.out.println(password);
        System.out.println(password2);
    }

    @Test
    void decode() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean match = bCryptPasswordEncoder.matches("user", "$2a$10$zULkgD/8BXFtzh5xaTvX6uWe/XZynNHT6Sz5dv2PYUP/nNgffJ1Oa");
        boolean match2 = bCryptPasswordEncoder.matches("user", "$2a$10$Wpy4eXw5FcsihAxGnHal8.3hVYd1heLEdBk8s6UBrOwIToaPnOkZu");
        System.out.println(match);
        System.out.println(match2);
    }

}
