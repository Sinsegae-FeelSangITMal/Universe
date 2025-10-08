package com.sinse.universe.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String store(MultipartFile file, String baseDir) throws IOException;
}
