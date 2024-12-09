package org.crews.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.crews.jwt.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.security.SecureRandom;

@Component
@RequestScope
public class AuthUtil {

    private final JWTUtil jwtUtil;
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?/";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

    public AuthUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getMemberId(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null ) return null;

        try {
            String token = authorizationHeader.substring(7);
            return jwtUtil.getMemberId(token);
        } catch (Exception e) {
            return null;
        }
    }

    public String getMemberRole(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null ) return null;

        try {
            String token = authorizationHeader.substring(7);
            return jwtUtil.getRole(token);
        } catch (Exception e) {
            return null;
        }
    }


    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALL_CHARACTERS.length());
            password.append(ALL_CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public static String verifyRandomNumber() {
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(1000000);
        return String.format("%06d", randomNumber);
    }
}
