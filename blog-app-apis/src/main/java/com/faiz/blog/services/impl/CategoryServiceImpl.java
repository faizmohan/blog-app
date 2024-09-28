package com.faiz.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faiz.blog.entities.Category;
import com.faiz.blog.exceptions.ResourceNotFoundException;
import com.faiz.blog.payloads.CategoryDto;
import com.faiz.blog.repositories.CategoryRepository;
import com.faiz.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Category dtoToCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		return category;
	}
	
	private CategoryDto categoryToDto(Category category) {
		CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
		return categoryDto;
	}

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		Category category = this.dtoToCategory(categoryDto);
		Category savedCategory = this.categoryRepository.save(category);
		
		return this.categoryToDto(savedCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, int id) {
		
		Category category = this.categoryRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Category", "id", id));
		category.setCategoryName(categoryDto.getCategoryName());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		
		Category updatedCategory = this.categoryRepository.save(category);
		
		return this.categoryToDto(updatedCategory);
	}

	@Override
	public CategoryDto getCategoryById(int id) {
		
		Category category = this.categoryRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Category", "id", id));
		
		return this.categoryToDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		
		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDto> categoryDtos =  categories.stream().map(category -> this.categoryToDto(category)).collect(Collectors.toList());
		
		return categoryDtos;
	}

	@Override
	public void deleteCategory(int id) {
		
		Category category = this.categoryRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Category", "id", id));
		this.categoryRepository.delete(category);
		
	}	
}
