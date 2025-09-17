package com.sinse.universe.model;

import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.util.CodeGenerator;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {
    private final EmailServiceImpl emailService;

    public AuthServiceImpl(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    // 서비스 계층에서 예외 발생 시 도메인 예외로 바꿔서 컨트롤러로 전달
    public void sendVerificationCode(String fromEmail) {
        try {
            //Todo: 이메일 중복확인
            // 만약 중복이면 잡지말고 그냥 throw CustomException하면됨


            String code = CodeGenerator.generate6DigitCode();
            //emailService.sendMail(fromEmail, "Universe 인증 코드", code);
            // Todo: redis에 코드 저장


        } catch (MailException e) {
            throw new CustomException(ErrorCode.MAIL_SEND_FAILED);
        }
    }
}
