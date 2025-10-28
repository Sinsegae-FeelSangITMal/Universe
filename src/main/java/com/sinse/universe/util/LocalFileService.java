package com.sinse.universe.util;

import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Profile("local-storage") // 이 프로필이 활성화될 때만 사용
public class LocalFileService implements StorageService {

    @Value("${upload.base-dir:C:/upload}")
    private String baseDirRoot; // 로컬 저장 루트 (예: C:/upload)

    /** dir은 논리 경로(예: recording/s68). 반환값은 항상 "dir/filename" 형태의 키 */
    @Override
    public String store(MultipartFile file, String dir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
        }

        String safeDir = normalizeLogical(dir);                 // recording/s68
        String ext = extractExt(file.getOriginalFilename());    // .webm 등
        String filename = UUID.randomUUID() + ext;              // 랜덤 파일명

        Path absDir = resolveUnderRoot(safeDir);                // C:/upload/recording/s68
        Files.createDirectories(absDir);

        Path target = absDir.resolve(filename).normalize();     // C:/upload/recording/s68/<uuid>.webm
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String key = joinLogical(safeDir, filename);            // recording/s68/<uuid>.webm
        log.info("[LOCAL] stored: {} -> {}", key, target);
        return key;
    }

    /** S3와 동일 시그니처: key는 논리 키(예: recording/s68/rec_abc.webm) */
    @Override
    public InputStream getObjectStream(String key) {
        String safeKey = normalizeLogical(Objects.requireNonNull(key));
        Path abs = resolveUnderRoot(safeKey);
        try {
            return Files.newInputStream(abs, StandardOpenOption.READ);
        } catch (NoSuchFileException e) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND); // 프로젝트 공통 코드에 맞춰 사용
        } catch (IOException e) {
            log.warn("[LOCAL] read fail {} -> {}", safeKey, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /** 단일 객체 삭제 (존재하지 않으면 무시) */
    @Override
    public void deleteObject(String key) {
        if (key == null || key.isBlank()) return;
        String safeKey = normalizeLogical(key);
        Path abs = resolveUnderRoot(safeKey);
        try {
            Files.deleteIfExists(abs);
            log.info("[LOCAL] deleted: {}", abs);
        } catch (IOException e) {
            log.warn("[LOCAL] delete fail {} -> {}", abs, e.getMessage());
        }
    }

    /** prefix 하위 전체 삭제 (예: recording/s68) */
    @Override
    public void deleteFolderPrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) return;
        String safePrefix = normalizeLogical(prefix.endsWith("/") ? prefix.substring(0, prefix.length()-1) : prefix);
        Path dir = resolveUnderRoot(safePrefix);

        if (!Files.exists(dir)) return;

        try {
            // 하위부터 삭제
            Files.walk(dir)
                    .sorted((a, b) -> b.compareTo(a)) // 하위 → 상위
                    .forEach(p -> {
                        try { Files.deleteIfExists(p); }
                        catch (IOException e) { log.warn("[LOCAL] delete fail {} -> {}", p, e.getMessage()); }
                    });
            log.info("[LOCAL] deleted prefix: {}", dir);
        } catch (IOException e) {
            log.warn("[LOCAL] delete prefix fail {} -> {}", dir, e.getMessage());
        }
    }

    /* ===== 유틸 ===== */

    private String extractExt(String original) {
        if (original == null) return "";
        int dot = original.lastIndexOf('.');
        return (dot >= 0 ? original.substring(dot) : "");
    }

    /** 논리 경로를 안전하게 정규화 (백슬래시→슬래시, 앞뒤 슬래시 제거, '..' 방지) */
    private String normalizeLogical(String logical) {
        String s = logical.replace('\\', '/').trim();
        while (s.startsWith("/")) s = s.substring(1);
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        if (s.contains("..")) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
        }
        return s;
    }

    /** 논리 경로를 루트 아래 실제 절대경로로 변환 (루트 이탈 방지) */
    private Path resolveUnderRoot(String logicalPath) {
        Path root = Paths.get(baseDirRoot).toAbsolutePath().normalize();            // C:/upload
        Path abs = root.resolve(logicalPath.replace('\\', '/')).normalize();        // C:/upload/recording/s68/...
        if (!abs.startsWith(root)) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
        }
        return abs;
    }

    private String joinLogical(String a, String b) {
        String left = (a == null ? "" : a.replace('\\', '/'));
        String right = (b == null ? "" : b.replace('\\', '/'));
        if (left.endsWith("/")) left = left.substring(0, left.length() - 1);
        if (right.startsWith("/")) right = right.substring(1);
        return left.isBlank() ? right : left + "/" + right;
    }
}
