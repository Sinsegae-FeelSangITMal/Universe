package com.sinse.universe.controller;

import com.sinse.universe.dto.request.ChatMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class ChatController {

    /**
     * 클라이언트가 /app/live/{artistId} 로 보낸 메시지를 받아서
     * /topic/public/{artistId} 로 브로드캐스트
     */
    @MessageMapping("/live/{artistId}")
    @SendTo("/topic/public/{artistId}")
    public ChatMessageRequest sendMessage(@DestinationVariable int artistId,
                                          ChatMessageRequest msg) {
        // timestamp 서버에서 강제 세팅
        ChatMessageRequest withTime = new ChatMessageRequest(
                artistId,
                msg.sender(),
                msg.message(),
                LocalDateTime.now() // 이후에 sent_at - live_start_at = delta 로 다시보기
        );

        log.debug(" sender = " +  msg.sender() + " message = " + msg.message());

        // DB 저장 나중에 구현
//        chatMessageService.save(withTime);

        // 브로드캐스트
        return withTime;
    }
}
