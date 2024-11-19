package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MemberDetails;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Member;
import org.crews.repository.MemberRepository;
import org.crews.utils.AESUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AESUtil aesUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("로그인 시도 이메일: {}", email);
        String encryptedEmail;
        try {
            encryptedEmail = aesUtil.encrypt(email);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.EMAIL_ENCRYPTION_FAILED,e);
        }

        Optional<Member> optionalMember = Optional.empty();
        try{
            optionalMember = memberRepository.findByEmail(encryptedEmail);
            log.info(String.valueOf(optionalMember));
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        if (!optionalMember.isPresent()) {
            log.info("사용자를 찾을 수 없음");
            throw new CustomException(ErrorCode.USER_NOT_FOUND,email);
        }

        Member member = optionalMember.get();

       return new MemberDetails(member);
    }


}
