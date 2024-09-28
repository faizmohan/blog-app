package com.faiz.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faiz.blog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
