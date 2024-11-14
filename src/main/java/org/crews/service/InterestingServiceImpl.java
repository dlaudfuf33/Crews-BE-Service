package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.InterestingResponseDto;
import org.crews.repository.InterestingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class InterestingServiceImpl implements InterestingService {
    private final InterestingRepository interestingRepository;

    @Override
    public List<InterestingResponseDto> getAllInterestings() {
        return interestingRepository.findAll().stream().map(InterestingResponseDto::of).toList();
    }

}
