package com.sinse.universe.model.auth;

import java.util.Map;
import java.util.Optional;

/**
 * OAuth2 provider별로 서로 다른 응답(JSON 구조)을 통일된 형태로 매핑
 * @param oauth_id provider 내 사용자의 고유 식별자
 * @param email    provider가 제공하는 email
 */

public record OAuth2UserInfo (
        String oauth_id,
        String email
){
    public static Optional<OAuth2UserInfo> of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> Optional.of(ofGoogle(attributes));
            default -> Optional.empty();
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return new OAuth2UserInfo(
                (String) attributes.get("sub"),
                (String) attributes.get("email")
        );
    }
}