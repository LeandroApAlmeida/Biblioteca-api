package br.com.leandro.library.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.leandro.library.model.User;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    
	
	private static final String ISSUER = "library-api";
	
	// Deixo a chave no código somente para fins didáticos.
    private String SECRET = "LK45HG6A6J6B66J7OS90C0QW12";
	
	
    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(user.getUsername())
        .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")))
        .sign(algorithm);
        return token;
    }
    
    
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build()
            .verify(token)
            .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
    
    
    public String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
    
}
