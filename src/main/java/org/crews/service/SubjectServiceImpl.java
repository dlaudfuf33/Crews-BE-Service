package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.SubjectsResponse;
import org.crews.model.Subject;
import org.crews.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    @Transactional(readOnly = true)
    public SubjectsResponse getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAllWithInterestings();
        return SubjectsResponse.from(subjects);
    }
}
