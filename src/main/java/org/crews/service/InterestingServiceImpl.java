package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.InterestingResponse;
import org.crews.repository.InterestingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class InterestingServiceImpl implements InterestingService {
    private final InterestingRepository interestingRepository;

    @Override
    public List<InterestingResponse> getAllInterestings() {
        return interestingRepository.findAll().stream().map(InterestingResponse::from).toList();
    }

}
