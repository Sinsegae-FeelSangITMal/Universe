package com.sinse.universe.model.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// 이메일 전송과 관련한 서비스
@Service
public class EmailServiceImpl {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 메일 전송
     * @param to       수신이메일
     * @param subject  제목
     * @param text     내용
     */
    public void sendMail(String to, String subject, String text) {
        // 단순 텍스트 메일 (HTML 메일은 MimeMessage 사용)
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        mail.setFrom(from);

        javaMailSender.send(mail);
    }
}
