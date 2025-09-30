package com.sinse.universe.dto.request;


import java.time.LocalDateTime;

public record ChatMessageRequest(
        int artistId,
        String sender,
        String message,
        LocalDateTime timestamp
) {}