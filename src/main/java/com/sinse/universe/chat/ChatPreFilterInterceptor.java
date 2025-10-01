package com.sinse.universe.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import java.security.Principal;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatPreFilterInterceptor implements ChannelInterceptor {

    private final RateLimiterService rateLimiter;     // 버킷/Redis 기반
    private final HtmlSanitizer sanitizer;            // Jsoup/OWASP sanitizer
    private final ChatModerationService moderation;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(acc.getCommand())) {
            // 엔드포인트에 따라 필터 적용
            String dest = acc.getDestination(); // 예: /app/live/11
            if (dest != null && dest.startsWith("/app/live/")) {
                String payload = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);

                // 1) 속도 제한(브라우저/세션/IP 기준)
                String senderKey = Optional.ofNullable(acc.getUser())
                        .map(Principal::getName)
                        .orElse(acc.getSessionId());
                if (!rateLimiter.allow(senderKey)) {
                    throw new MessagingException("RATE_LIMITED");
                }

                // 2) XSS/HTML 제거
                String safe = sanitizer.clean(payload);

                // 3) 욕설/링크 필터
                ChatModerationService.ModerationResult r = moderation.moderate(safe);
                if (!r.allowed()) {
                    throw new MessagingException("BLOCKED_CONTENT");
                }

                // 정화된 payload로 교체
                return MessageBuilder.createMessage(
                        r.cleaned().getBytes(StandardCharsets.UTF_8),
                        message.getHeaders()
                );
            }
        }
        return message;
    }
}
