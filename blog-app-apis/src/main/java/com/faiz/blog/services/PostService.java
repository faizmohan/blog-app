package com.faiz.blog.services;

import java.util.List;

import com.faiz.blog.payloads.PostDto;
import com.faiz.blog.payloads.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto, int categoryId, int userId);
	PostDto updatePost(PostDto postDto, int id);
	PostDto getPostById(int id);
	PostResponse getAllPost(int pageNumber, int pageSize, String sortBy, String sortDir);
	void deletePost(int id);
	List<PostDto> getPostByCategory(int categoryId);
	List<PostDto> getPostByUser(int userId);
	List<PostDto> searchPost(String keyWord);
	
}
