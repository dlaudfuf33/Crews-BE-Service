package org.crews.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.LoginRequest;
import org.crews.dto.MemberDetails;
import org.crews.model.RefreshEntity;
import org.crews.repository.RefreshRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청의 Content-Type이 application/json인지 확인
            if ("application/json".equals(request.getContentType())) {
                ObjectMapper objectMapper = new ObjectMapper();
                LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

                String email = loginRequest.getEmail();
                String password = loginRequest.getPassword();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

                // AuthenticationManager를 사용하여 인증 시도
                return authenticationManager.authenticate(authToken);
            } else {
                // 기본 처리 (폼 데이터)
                return super.attemptAuthentication(request, response);
            }
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new AuthenticationServiceException("로그인 요청을 처리하는 동안 오류 발생", e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        Long memberId = memberDetails.getMember().getId();

        String access = jwtUtil.createJwt("access", email, role, memberId,600000L);
        String refresh = jwtUtil.createJwt("refresh", email, role, memberId,864000000L);

        //Refresh 토큰 저장
        addRefreshEntity(email, refresh, 86400000L);

        // 응답설정
        response.addHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        refreshRepository.deleteByUsername(username);
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        // https 통신을 할 경우 설정
//        cookie.setSecure(true);
        // 적용될 범위 설정
//        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }


}