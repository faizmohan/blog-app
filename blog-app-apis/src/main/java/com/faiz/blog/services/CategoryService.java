package com.faiz.blog.services;

import java.util.List;

import com.faiz.blog.payloads.CategoryDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryDto categoryDto, int id);
	CategoryDto getCategoryById(int id);
	List<CategoryDto> getAllCategory();
	void deleteCategory(int id);
}
