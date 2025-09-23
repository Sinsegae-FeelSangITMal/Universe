package com.sinse.universe.controller;

import com.sinse.universe.dto.request.ProductRegistRequest;
import com.sinse.universe.model.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 - 메인/상세 이미지 포함 시 201, Location, ApiResponse 본문 검증")
    void registerProduct_withFiles_returns201CreatedAndLocationAndBody() throws Exception {
        // given
        int createdId = 123;
        when(productService.regist(
                any(ProductRegistRequest.class),
                ArgumentMatchers.<org.springframework.web.multipart.MultipartFile>any(),
                any(List.class))
        ).thenReturn(createdId);

        MockMultipartFile main = new MockMultipartFile(
                "mainImage", "main.jpg", "image/jpeg", "main-content".getBytes()
        );
        MockMultipartFile detail1 = new MockMultipartFile(
                "detailImages", "d1.jpg", "image/jpeg", "detail1".getBytes()
        );
        MockMultipartFile detail2 = new MockMultipartFile(
                "detailImages", "d2.jpg", "image/jpeg", "detail2".getBytes()
        );

        // when & then
        mockMvc.perform(
                        multipart("/api/ent/products")
                                .file(main)
                                .file(detail1)
                                .file(detail2)
                                // 아래 param들은 @ModelAttribute ProductRegistRequest 바인딩용 (필드명에 맞게 수정)
                                .param("categoryId", "1")
                                .param("artistId", "2")
                                .param("productName", "테스트 상품")
                                .param("price", "10000")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/ent/products/" + createdId))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("등록 성공"))
                .andExpect(jsonPath("$.data.id").value(createdId));

        verify(productService).regist(
                any(ProductRegistRequest.class),
                ArgumentMatchers.<org.springframework.web.multipart.MultipartFile>any(),
                any(List.class)
        );
    }

    @Test
    @DisplayName("상품 등록 - 상세 이미지 없이도 201 반환")
    void registerProduct_withoutDetailImages_returns201Created() throws Exception {
        // given
        int createdId = 456;
        when(productService.regist(
                any(ProductRegistRequest.class),
                ArgumentMatchers.<org.springframework.web.multipart.MultipartFile>any(),
                any(List.class))
        ).thenReturn(createdId);

        MockMultipartFile main = new MockMultipartFile(
                "mainImage", "main.jpg", "image/jpeg", "main-content".getBytes()
        );

        // when & then
        mockMvc.perform(
                        multipart("/api/ent/products")
                                .file(main)
                                .param("categoryId", "1")
                                .param("artistId", "2")
                                .param("productName", "상세 없이")
                                .param("price", "5000")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/ent/products/" + createdId))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("등록 성공"))
                .andExpect(jsonPath("$.data.id").value(createdId));

        verify(productService).regist(
                any(ProductRegistRequest.class),
                ArgumentMatchers.<org.springframework.web.multipart.MultipartFile>any(),
                any(List.class)
        );
    }
}
