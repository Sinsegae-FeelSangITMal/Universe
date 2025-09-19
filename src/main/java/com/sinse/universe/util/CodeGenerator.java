package com.sinse.universe.util;

import java.security.SecureRandom;

// 인증 코드, 임시 비밀번호 발급 등 코드 생성 전용 유틸
public class CodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generate6DigitCode() {
        // 0 ~ 999999 사이의 난수 생성
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }
}

