package org.crews.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.crews.jwt.JWTUtil;
import org.springframework.stereotype.Component;

@Component
public class    AuthUtil {

    private final JWTUtil jwtUtil;

    public AuthUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getMemberId(HttpServletRequest request){
        return jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));
    }
}
