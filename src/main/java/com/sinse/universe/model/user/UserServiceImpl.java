package com.sinse.universe.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkDuplicateEmail(String loginId){
        return userRepository.existsByLoginId(loginId);
    }
}
