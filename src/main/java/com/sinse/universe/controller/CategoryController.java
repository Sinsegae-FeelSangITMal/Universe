package com.sinse.universe.controller;

import com.sinse.universe.domain.Category;
import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.dto.response.CategoryResponse;
import com.sinse.universe.model.category.CategoryRepository;
import com.sinse.universe.model.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public  CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/ent/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAll() {
        List<Category> categories = categoryService.findAll(); // List<Category>
        List<CategoryResponse> dtoList = categories.stream()
                .map(CategoryResponse::from)
                .toList();
        return ApiResponse.success("카테고리 조회 성공", dtoList);
    }
}
