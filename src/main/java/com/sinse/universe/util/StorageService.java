package com.sinse.universe.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    String store(MultipartFile file, String baseDir) throws IOException;


    void deleteObject(String key);

    void deleteFolderPrefix(String prefix);

    /** S3/Object Storage에서 객체 읽기용 스트림 반환 */
    InputStream getObjectStream(String key);
}
