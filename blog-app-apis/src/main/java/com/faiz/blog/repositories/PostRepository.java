package com.faiz.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faiz.blog.entities.Category;
import com.faiz.blog.entities.Post;
import com.faiz.blog.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	List<Post> findByCategory(Category category);
	List<Post> findByUser(User user);
	List<Post> findByTitleContaining(String title);
	
//	@Query("select p from post p where p.title like :key")
//	List<Post> searchPost(@Param("key") String title);
}
