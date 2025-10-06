package com.sinse.universe.model.user;

import com.sinse.universe.domain.PartnerUser;
import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.UserJoinRequest;
import com.sinse.universe.dto.response.PartnerUserResponse;
import com.sinse.universe.dto.response.UserResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.enums.UserRole;
import com.sinse.universe.enums.UserStatus;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.PartnerUserRepository;
import com.sinse.universe.model.auth.OAuth2UserInfo;
import com.sinse.universe.model.role.RoleServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl {

    private final RoleServiceImpl roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PartnerUserRepository partnerUserRepository;

    public UserServiceImpl(RoleServiceImpl roleService, UserRepository userRepository, PasswordEncoder passwordEncoder, PartnerUserRepository partnerUserRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.partnerUserRepository = partnerUserRepository;
    }

    public void checkDuplicateEmail(String email){
        if(userRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public User createGeneralUser(UserJoinRequest form) {
        User user = User.builder()
                .email(form.email())
                .name(form.name())
                .password(passwordEncoder.encode(form.password())) // 암호화
                .role(roleService.findByName(form.role()))
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public User createOAuthUser(String provider, OAuth2UserInfo userInfo) {
        User user = User.builder()
                .provider(provider)
                .email(userInfo.email())
                .oauthId(userInfo.oauth_id())
                .status(UserStatus.INCOMPLETE)
                .role(roleService.findByName(UserRole.USER))
                .build();

        return userRepository.save(user);
    }

    public UserResponse getUserInfo(int userId) {
        User user = userRepository.findById(userId).get();
        return UserResponse.from(user);
    }

    public PartnerUserResponse getPartnerUserInfo(int userId) {
        PartnerUser partnerUser = partnerUserRepository.findByUserId(userId).get();
        return PartnerUserResponse.from(partnerUser);
    }
}
