package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.UserRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public User getReference(UUID id) {
        return userRepository.getReferenceById(id);
    }
}
