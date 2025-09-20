package com.sinse.universe.model.user;

import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDuplicateEmail(String loginId){
        if(userRepository.existsByLoginId(loginId)){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public boolean checkDuplicateEmail(String loginId){
        return userRepository.existsByLoginId(loginId);
    }
}
