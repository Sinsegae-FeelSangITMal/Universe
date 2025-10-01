package com.sinse.universe.controller;

import com.sinse.universe.chat.ChatModerationService;
import com.sinse.universe.dto.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatModerationService moderation; // 주입

    /**
     * 클라이언트가 /app/live/{artistId} 로 보낸 메시지를 받아서
     * /topic/public/{artistId} 로 브로드캐스트
     *
     * - 들어온 메시지를 정화/판정한다.
     * - BLOCK이면 MessagingException을 던져 전송을 거부한다.
     * - MASK/ALLOW면 정화된 본문으로 치환하여 브로드캐스트한다.
     * - 타임스탬프는 서버에서 강제 세팅한다.
     */
    @MessageMapping("/live/{artistId}")
    @SendTo("/topic/public/{artistId}")
    public ChatMessageRequest sendMessage(@DestinationVariable int artistId,
                                          ChatMessageRequest msg) {
        if (msg == null) {
            throw new MessagingException("INVALID_PAYLOAD");
        }

        // 0) 닉네임/본문 기본 정리
        String rawSender  = safeNickname(msg.sender());
        String rawMessage = Objects.toString(msg.message(), "");

        // 1) 모더레이션 (정화 + 금칙/스팸 판정)
        ChatModerationService.ModerationResult result = moderation.moderate(rawMessage);

        if (!result.allowed()) {
            // 금칙/스팸 → 전송 거부
            log.debug("[CHAT] blocked: artistId={}, sender={}, reason={}", artistId, rawSender, result.reason());
            throw new MessagingException("CONTENT_BLOCKED");
        }

        // 2) 허용/마스킹된 결과를 사용
        String cleaned = result.cleaned(); // 정화/마스킹된 텍스트

        // 3) 서버 타임스탬프 부여 (replay 동기화를 위해 서버 시간이 기준)
        ChatMessageRequest withTime = new ChatMessageRequest(
                artistId,
                rawSender,
                cleaned,
                LocalDateTime.now()
        );

        log.debug("[CHAT] ok: artistId={}, sender={}, message={}", artistId, rawSender, cleaned);

        // (선택) 여기서 비동기 큐/배치로 DB 적재 enqueue 가능
        // chatPersistProducer.enqueue(withTime);

        return withTime; // @SendTo 로 브로드캐스트
    }

    /**
     * 닉네임 정화: HTML 제거 + 길이 제한 + 공백 트림
     */
    private String safeNickname(String in) {
        String s = in == null ? "익명" : in;
        // HTML / XSS 제거
        s = Jsoup.clean(s, Safelist.none());
        // 트림 + 길이 제한(이상한 도배 닉 방지)
        s = s.trim();
        if (s.isEmpty()) s = "익명";
        if (s.length() > 20) s = s.substring(0, 20);
        return s;
    }
}
