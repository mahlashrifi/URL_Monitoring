package Controllers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JWTController {
    // Key is hardcoded here for simplicity.
    // Ideally this will get loaded from env configuration/secret vault
    private String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    public JWTController() {
    }

    public String createJWT(String userName, String pass) {

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("userName", userName)
                .claim("pass", pass)
                //.setSubject("jane")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5l, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwtToken;
    }


    public Jws<Claims> parseJwt(String jwtString) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parser()
                .setSigningKey(hmacKey)
                .parseClaimsJws(jwtString);
        return jwt;

    }

    public String extractUserNameFromJWT(String token) {
        Jws<Claims> jwt = parseJwt(token);
        Pattern pattern = Pattern.compile("userName=(\\w+),");
        Matcher matcher = pattern.matcher(jwt.toString());
        if(matcher.find()) {
            return matcher.group(1);
        }
        else return null;
    }



    }







