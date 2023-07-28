package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exp.AppMethodNotAllowedException;
import com.example.exp.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            String jwt = authToken.substring(7);
            return JWTUtil.decode(jwt);
        }
        throw new UnAuthorizedException("Not authorized");
    }

    public static JwtDTO hasRole(HttpServletRequest request, ProfileRole... requiredRoles) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        if (requiredRoles == null) {
            return new JwtDTO(id, role);
        }
        boolean found = false;
        for (ProfileRole required : requiredRoles) {
            if (role.equals(required)) {
                found = true;
            }
        }
        if (!found) {
            throw new AppMethodNotAllowedException();
        }
        return new JwtDTO(id, role);
    }

    public static JwtDTO hasRole(String authToken, ProfileRole... requiredRoles) {
        JwtDTO jwtDTO = getJwtDTO(authToken);
        if(requiredRoles == null){
            return jwtDTO;
        }
        boolean found = false;

        for (ProfileRole role : requiredRoles) {
            if (jwtDTO.getRole().equals(role)) {
                found = true;
            }
        }
        if (!found) {
            throw new AppMethodNotAllowedException();
        }
        return jwtDTO;
    }
}
