package com.example.security.dto;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        if (claims != null) {
            return claimsResolver.apply(claims);
        }
        return null;
    }


    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    ///判断token是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    ///生成token
    public String generateToken(UserDetails userDetails){
        Map<String,Object>claims=new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        return  doGenerateToken(claims,userDetails.getUsername());
    }





    private String doGenerateToken(Map<String,Object>claims,String subject){
        Header header=Jwts.header();
        header.setType("JWT");
        return  Jwts.builder().setHeader((Map<String,Object>)header).setClaims(claims).setSubject(subject)
                .setIssuer("??").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*2*1000))
                .signWith(SignatureAlgorithm.ES512,secret).compact();
    }

    ///刷新token
    private String doGenerateRefreshToken(Map<String,Object>claims,String subject){
        Header header=Jwts.header();
        header.setType("JWT");
        return  Jwts.builder().setHeader((Map<String,Object>)header).setClaims(claims).setSubject(subject)
                .setIssuer("??").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*2*1000))
                .signWith(SignatureAlgorithm.ES512,secret).compact();
    }


    public Boolean validateToken(String token,UserDetails userDetails){
        final String userName=getUsernameFromToken(token);

        if(userName!=null){
            return (userName.equals(userDetails.getUsername())&&!isTokenExpired(token));
        }

        return false;
    }

}
