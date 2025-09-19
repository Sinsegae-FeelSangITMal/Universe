package com.sinse.universe.model.auth;

import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.user.UserServiceImpl;
import com.sinse.universe.model.common.EmailServiceImpl;
import com.sinse.universe.util.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class AuthServiceImpl {

    // 스프링 부트는 Duration 바인딩을 지원. (ms, s, m, h 등 단위 가능)
    // email.verify.ttl=3m  -> 3분
    @Value("${email.verify.ttl}")
    private Duration emailVerfiicationTtl;

    private final EmailServiceImpl emailService;
    private final UserServiceImpl userService;
    private final VerificationCodeRepository verificationCodeRepository;

    public AuthServiceImpl(EmailServiceImpl emailService, UserServiceImpl userService, VerificationCodeRepository verificationCodeRepository) {
        this.emailService = emailService;
        this.userService = userService;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    // 서비스 계층에서 예외 발생 시 도메인 예외로 바꿔서 컨트롤러로 전달
    public void sendVerificationCode(String toEmail) {
        try {
            //이메일 중복확인
            if(userService.checkDuplicateEmail(toEmail)){
                throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
            }

            // 인증 코드 생성, 전송 redis에 저장
            String code = CodeGenerator.generate6DigitCode();
            emailService.sendMail(toEmail, "Universe 인증 코드", code);
            verificationCodeRepository.saveCode(toEmail, code, emailVerfiicationTtl);

            log.info("이메일 인증 코드 전송 완료 - to={}, code={}", toEmail, code);
        } catch (MailException e) {
            throw new CustomException(ErrorCode.MAIL_SEND_FAILED, e);
        }
    }
}
