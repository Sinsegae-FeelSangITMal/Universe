package com.sinse.universe.model.auth;

import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.user.UserServiceImpl;
import com.sinse.universe.model.common.EmailServiceImpl;
import com.sinse.universe.util.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class AuthServiceImpl {

    // 스프링 부트는 Duration 바인딩을 지원. (ms, s, m, h 등 단위 가능)
    // email.verify.ttl=3m  -> 3분
    @Value("${email.verification-code.ttl}")
    private Duration verfiicationCodeTtl;

    @Value("${email.verified-email.ttl}")
    private Duration verifiedEmailTtl;

    private final EmailServiceImpl emailService;
    private final UserServiceImpl userService;
    private final VerificationEmailRepository verificationEmailRepository; //레디스 저장소

    public AuthServiceImpl(EmailServiceImpl emailService, UserServiceImpl userService, VerificationEmailRepository verificationCodeRepository) {
        this.emailService = emailService;
        this.userService = userService;
        this.verificationEmailRepository = verificationCodeRepository;
    }

    public void sendVerificationCode(String toEmail) {
        userService.checkDuplicateEmail(toEmail);  //이메일 중복확인

        // 인증 코드 생성, 전송 redis에 저장
        String code = CodeGenerator.generate6DigitCode();
        emailService.sendMail(toEmail, "Universe 인증 코드", code);
        verificationEmailRepository.saveCode(toEmail, code, verfiicationCodeTtl);

        log.info("이메일 인증 코드 전송 완료 - to={}, code={}", toEmail, code);
    }

    public void verifyEmail(String email, String inputCode){
        // 코드 시간 만료
        String storedCode = verificationEmailRepository.getCode(email)
                .orElseThrow(()-> new CustomException(ErrorCode.VERIFICATION_CODE_EXPIRED));
        // 코드 불일치
        if(!storedCode.equals(inputCode)){
            throw new CustomException(ErrorCode.VERIFICATION_CODE_INVALID);
        }
        verificationEmailRepository.saveVerifiedEmail(email, verifiedEmailTtl);
        log.info("이메일 인증 코드 검증 성공 verified={}", email);
    }
}
