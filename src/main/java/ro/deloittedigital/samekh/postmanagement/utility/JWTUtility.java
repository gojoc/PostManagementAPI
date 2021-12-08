package ro.deloittedigital.samekh.postmanagement.utility;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTUtility {
    @Value(value = "${jwt.secret}")
    private String secret;

    public boolean isValid(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException exception) {
            log.info("[JWTUtility] expired JWT: {} - {}", jwt, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.info("[JWTUtility] unsupported JWT: {} - {}", jwt, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.info("[JWTUtility] malformed JWT: {} - {}", jwt, exception.getMessage());
        } catch (SignatureException exception) {
            log.info("[JWTUtility] invalid JWT signature: {} - {}", jwt, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.info("[JWTUtility] JWT payload is empty: {} - {}", jwt, exception.getMessage());
        }
        return false;
    }

    private Date getExpirationDate(String jwt) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();
    }

    public boolean isExpired(String jwt) {
        return getExpirationDate(jwt).before(new Date());
    }

    public String getEmail(String jwt) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
