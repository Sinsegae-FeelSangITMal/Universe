package com.sinse.universe.model.auth;

import com.sinse.universe.domain.User;
import com.sinse.universe.model.user.UserRepository;
import com.sinse.universe.model.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


// provider에게 userinfo를 받아서 DB 조회

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // provider가 제공해준 user info를 담은 객체
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(provider, attributes)
                .orElseThrow(() -> new RuntimeException("지원하지 않는 provider=" + provider));

        // 기존 회원이 아니라면 강제 가입
        User user = userRepository.findByProviderAndOauthId(provider, userInfo.oauth_id())
                .orElseGet(()-> userService.createOAuthUser(provider, userInfo));

        // 기존 회원이라면. 혹시 이메일이 변경되었다면 최신으로 수정
        // JPA dirty checking - 수정사항이 있으면 DB에 자동 반영
        user.setEmail(userInfo.email());

        return new CustomOAuth2User(
                user,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())),
                attributes
        );
    }
}
