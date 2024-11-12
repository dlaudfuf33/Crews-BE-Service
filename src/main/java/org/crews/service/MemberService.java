package org.crews.service;

import org.crews.dto.MemberRequest;
import org.crews.dto.MemberResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MemberService {

    MemberResponse signUp(MemberRequest memberRequest);

    ResponseEntity<String> refreshCheck(String refresh);

    Map<String, String> reissueTokens(String refresh);
}
