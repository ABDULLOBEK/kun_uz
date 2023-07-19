package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exp.AppMethodNotAllowedException;
import com.example.exp.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

import static javax.crypto.Cipher.SECRET_KEY;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            String jwt = authToken.substring(7);
            return JWTUtil.decode(jwt);
        }
        throw new UnAuthorizedException("Not authorized");
    }

    public static JwtDTO hasRole(String authToken, ProfileRole requiredRole) {
        JwtDTO jwtDTO = getJwtDTO(authToken);
        if (!jwtDTO.getRole().equals(requiredRole)) {
            throw new AppMethodNotAllowedException();
        }
        return jwtDTO;
    }
}
