package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.MemberDetails;
import org.crews.model.Member;
import org.crews.repository.MemberRepository;
import org.crews.utils.AESUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("로그인 시도 이메일: {}", email);
        String encryptedEmail = null;
        try {
            encryptedEmail = AESUtil.encrypt(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("암호화된 이메일: {}", encryptedEmail);

        Optional<Member> optionalMember = null;
        try{
            optionalMember = memberRepository.findByEmail(encryptedEmail);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        if (optionalMember.isPresent()) {
            log.info("DB에서 조회된 사용자: {}", optionalMember.get());
        } else {
            log.info("사용자를 찾을 수 없음");
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Member member = optionalMember.get();

       return new MemberDetails(member);
    }


}
