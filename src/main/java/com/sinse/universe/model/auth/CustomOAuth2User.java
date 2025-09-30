package com.sinse.universe.model.auth;

import com.sinse.universe.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


// provider에게 받은 userInfo + DB 회원 정보를 함께 가지고 있는 객체

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    @Getter
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getOauthId(); //시큐리티 내부에서 사용할 식별값
    }
}
