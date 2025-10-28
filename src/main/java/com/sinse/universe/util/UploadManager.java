package com.sinse.universe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class UploadManager {

    private static StorageService storageService;

    @Autowired
    public void setStorageService(StorageService storageService) {
        UploadManager.storageService = storageService;
    }

    /**
     * 범용 파일 저장 메서드.
     * 실제 저장은 주입된 StorageService 구현체에 위임한다.
     * @param file 업로드할 파일
     * @param baseDir 저장할 디렉터리
     * @return 저장된 파일의 키 (예: product/main/uuid.png)
     */
    public static String storeAndReturnName(MultipartFile file, String baseDir) throws IOException {
        if (storageService == null) {
            throw new IllegalStateException("StorageService is not initialized. Check Spring configuration.");
        }
        return storageService.store(file, baseDir);
    }
}
