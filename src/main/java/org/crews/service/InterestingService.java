package org.crews.service;

import org.crews.dto.response.InterestingResponse;

import java.util.List;

public interface InterestingService {
    // 모든 Interesting 항목 조회
    List<InterestingResponse> getAllInterestings();

}
