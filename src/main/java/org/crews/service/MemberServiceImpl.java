package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.LoginRequest;
import org.crews.dto.MemberDetails;
import org.crews.dto.MemberRequest;
import org.crews.dto.MemberResponse;
import org.crews.model.Member;
import org.crews.repository.MemberRepository;
import org.crews.utils.AESUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public MemberResponse signUp(MemberRequest memberRequest) throws Exception {
        Boolean isExist = memberRepository.existsByEmail(AESUtil.encrypt(memberRequest.getEmail()));

        if (isExist) {
            return null;
        }

        Member member = Member.from(memberRequest);
        member.setEmail(AESUtil.encrypt(member.getEmail()));
        member.setName(AESUtil.encrypt(member.getName()));
        member.setPhoneNumber(AESUtil.encrypt(member.getPhoneNumber()));
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);

    }

    @Override
    public MemberDetails login(LoginRequest loginRequest) throws Exception {
        String encryptedEmail = AESUtil.encrypt(loginRequest.getEmail());
        Member member = memberRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        return new MemberDetails(member);
    }
}
