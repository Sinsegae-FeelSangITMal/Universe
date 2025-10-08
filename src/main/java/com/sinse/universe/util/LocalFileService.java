package com.sinse.universe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@Profile("local-storage") // 이 프로필이 활성화될 때만 이 Bean을 사용
public class LocalFileService implements StorageService {

    @Override
    public String store(MultipartFile file, String baseDir) throws IOException {
        Path dir = createDirectory(baseDir);

        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + ext;

        Path target = dir.resolve(newFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }

    private Path createDirectory(String path) throws IOException {
        Path dir = Paths.get(path);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir.toAbsolutePath().normalize();
    }
}
