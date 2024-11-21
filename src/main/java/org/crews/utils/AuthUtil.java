package org.crews.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.crews.jwt.JWTUtil;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final JWTUtil jwtUtil;

    public AuthUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getMemberId(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return 0L;

        Long memberId = jwtUtil.getMemberId(header.substring(7));
        if (memberId == null) return 0L;

        return memberId;
    }
}
