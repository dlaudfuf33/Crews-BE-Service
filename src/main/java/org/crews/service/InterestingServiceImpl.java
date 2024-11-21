package org.crews.service;


import lombok.RequiredArgsConstructor;
import org.crews.repository.InterestingRepository;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class InterestingServiceImpl implements InterestingService {
    private final InterestingRepository interestingRepository;

}
