package xcode.marsiajar.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.model.UserModel;

import java.util.Date;

import static xcode.marsiajar.shared.Utils.getTomorrowDate;

@Service
public class JwtService {

    /**
     * generate JWT Token for auth
     * @param user body
     * @return token
     */
    public String generateToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getSecureId())
                .setIssuedAt(new Date())
                .setExpiration(getTomorrowDate())
                .signWith(SignatureAlgorithm.HS256, "xcode")
                .compact();
    }
}
