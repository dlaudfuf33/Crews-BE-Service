package org.crews.service;

import org.crews.dto.LoginRequest;
import org.crews.dto.MemberDetails;
import org.crews.dto.MemberRequest;
import org.crews.dto.MemberResponse;

public interface MemberService {

    MemberResponse signUp(MemberRequest memberRequest) throws Exception;

    MemberDetails login(LoginRequest loginRequest) throws Exception;
}
