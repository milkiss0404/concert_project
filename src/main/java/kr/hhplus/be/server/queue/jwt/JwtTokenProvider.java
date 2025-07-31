package kr.hhplus.be.server.queue.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String createJwtToken(Long userId, Long concertId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("concertId", concertId)
                .claim("type", "QUEUE_TOKEN")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token); //여기서 유효성 검사
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        }
}