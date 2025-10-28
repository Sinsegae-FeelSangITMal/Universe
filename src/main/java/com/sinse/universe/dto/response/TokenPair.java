package com.sinse.universe.dto.response;

public record TokenPair (
        String accessToken,
        String refreshToken
){ }
