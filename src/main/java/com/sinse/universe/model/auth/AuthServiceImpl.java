package com.sinse.universe.model.auth;

import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.UserJoinRequest;
import com.sinse.universe.dto.response.TokenPair;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.user.UserRepository;
import com.sinse.universe.model.user.UserServiceImpl;
import com.sinse.universe.model.common.EmailServiceImpl;
import com.sinse.universe.util.CodeGenerator;
import com.sinse.universe.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    // 스프링 부트는 Duration 바인딩을 지원. (ms, s, m, h 등 단위 가능)
    // email.verify.ttl=3m  -> 3분
    @Value("${email.verification-code.ttl}")
    private Duration verfiicationCodeTtl;

    @Value("${email.verified-email.ttl}")
    private Duration verifiedEmailTtl;

    private final JwtUtil jwtUtil;
    private final EmailServiceImpl emailService;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final VerificationEmailRepository verificationEmailRepository; //레디스 저장소
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenPair reissueTokens(String refreshToken) {
        Jws<Claims> jws;
        String userId;

        // 검증 실패 -> userId를 신뢰할 수 없기때문에 삭제 로직은 수행하지 않음
        try {
            jws = jwtUtil.validateToken(refreshToken);
            userId = jws.getBody().getSubject();
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.INVALID_REFRESHTOKEN, e);
        }

        // redis에 토큰 없음 -> 이미 존재하지 않으니 삭제 불필요
        String storedToken = refreshTokenRepository.find(jws.getBody().getSubject())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESHTOKEN, "redis에 존재하지 않는 refreshtoken"));

        // 토큰 불일치 -> JWT는 유효하니 userId 신뢰 가능
        log.debug("클라이언트가 보낸 refreshToken={}", refreshToken);
        log.debug("Redis에 저장된 refreshToken={}", storedToken);
        if (!storedToken.equals(refreshToken)) {
            refreshTokenRepository.delete(storedToken);
            throw new CustomException(ErrorCode.INVALID_REFRESHTOKEN, "redis에 저장된 refresh token과 일치하지 않습니다.");
        }

        // 검증 성공 시 새 토큰 발급 로직
        User user = userRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.createAccessToken(user.getId(), user.getRole().getName().name(), user.getEmail());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

        refreshTokenRepository.save(userId, newRefreshToken, jwtUtil.getRefreshTokenTtl());

        return new TokenPair(newAccessToken, newRefreshToken);
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

    @Transactional
    public void join(UserJoinRequest form) {
        // 이메일 중복, 인증, 비밀번호 일치 검증
        userService.checkDuplicateEmail(form.email());

        if(!verificationEmailRepository.isVerified(form.email())){
            throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
        }
        if (!form.password().equals(form.passwordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCH);
        }

        User user = userService.createGeneralUser(form);
        log.info("회원가입 성공 user={}", user);
    }
}
