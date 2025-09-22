package com.sinse.universe.util;

import com.sinse.universe.domain.Product;
import com.sinse.universe.domain.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    public ProductImage store(MultipartFile file, Product product, String baseDir, ProductImage.Role role) throws IOException {
        createDirectory(baseDir);
        Path dir = createDirectory(baseDir + "/p" + product.getId());

        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + ext;

        Path target = dir.resolve(newFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        ProductImage img = new ProductImage();
        img.setProduct(product);
        img.setRole(role);
        img.setOriginalName(originalName);
        img.setMimeType(file.getContentType());
        img.setSize(file.getSize());
        img.setUrl("/images/product/" + product.getId() + "/" + newFilename);

        return img; // 저장은 서비스에서
    }


    // 디렉토리 생성 메서드 정의
    // createDirectory("c://upload"), createDirectory("p23")
    public Path createDirectory(String path) throws IOException {
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
