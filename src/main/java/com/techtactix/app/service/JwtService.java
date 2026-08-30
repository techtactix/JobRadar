package com.techtactix.app.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU29uZ2luZ21hc2sgZmF2b3I=";
	
//	private String secretKey;
	
//	public JwtService() {
//		secretKey=this.generateSecretKey();
//	}
	public String generateSecretKey() {
		 try {
		 KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
		 SecretKey secretKey = keyGen.generateKey();
		 System.out.println("Secret Key : " + secretKey.toString());
		 return Base64.getEncoder().encodeToString(secretKey.getEncoded());
		 } catch (NoSuchAlgorithmException e) {
		 throw new RuntimeException("Error generating secret key", e);
		 }
		}

	
	public String getToken(String username,String role) {
		
		Map<String, Object> claims=new HashMap<String, Object>();
		
		claims.put("role", role);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*12))
				.signWith(getkey(), SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getkey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }
	// helper to read role from token
	public String extractRole(String token) {
	    final Claims claims = extractAllClaims(token);
	    return claims.get("role", String.class);
	}

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getkey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}


//
//Jwts.builder()
//↓
//START BUILDING JWT
//↓
//┌─────────┴─────────┐
//↓                   ↓
//HEADER              PAYLOAD
//HS256 algorithm       username
//                      issuedAt
//                      expiration
//│                   │
//└─────────┬─────────┘
//↓
//.signWith(getkey(),
//HS256)
//↓
//SECRET KEY 🔑
//+
//HEADER + PAYLOAD
//↓
//HS256
//↓
//SIGNATURE
//↓
//.compact()
//↓
//HEADER.PAYLOAD.SIGNATURE
//↓
//JWT
