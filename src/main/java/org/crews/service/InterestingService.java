package org.crews.service;

import org.crews.dto.response.InterestingResponseDto;

import java.util.List;

public interface InterestingService {
    // 모든 Interesting 항목 조회
    List<InterestingResponseDto> getAllInterestings();

}
