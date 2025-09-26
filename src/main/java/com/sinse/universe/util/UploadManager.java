package com.sinse.universe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
public class UploadManager {
    /* ------------------------------------------
        범용 이미지 업로드 메서드 코드
        [input] baseDir : 파일 업로드 할 경로명
        [ouput] return값 : UUID로 바뀐 파일명 반환
        Service에서 반환받은 문자열로 db에 저장
    --------------------------------------------- */
    public static String storeAndReturnName(MultipartFile file, String baseDir) throws IOException {
        Path dir = createDirectory(baseDir);

        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + ext;

        Path target = dir.resolve(newFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return newFilename; // 파일명을 서비스단에 돌려줌
    }


    // 디렉토리 생성 메서드 정의
    // createDirectory("c://upload"), createDirectory("p23")
    public static Path createDirectory(String path) throws IOException {
        Path dir = Paths.get(path);
        Path savePath = Paths.get(path).toAbsolutePath().normalize();
        if(!(Files.exists(dir) && Files.isDirectory(dir))){
            Files.createDirectories(dir);
        } else {
            log.debug("{} 디렉토리가 이미 존재함 ", path);
        }
        log.debug("savePath 경로 : {}", savePath);
        return savePath;
    }
}
