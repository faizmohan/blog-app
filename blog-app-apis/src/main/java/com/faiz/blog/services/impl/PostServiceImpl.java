package com.faiz.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.faiz.blog.entities.Category;
import com.faiz.blog.entities.Post;
import com.faiz.blog.entities.User;
import com.faiz.blog.exceptions.ResourceNotFoundException;
import com.faiz.blog.payloads.PostDto;
import com.faiz.blog.payloads.PostResponse;
import com.faiz.blog.repositories.CategoryRepository;
import com.faiz.blog.repositories.PostRepository;
import com.faiz.blog.repositories.UserRepository;
import com.faiz.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Post dtoToPost(PostDto postDto) {
		Post post = this.modelMapper.map(postDto, Post.class);
		return post;
	}
	
	private PostDto postToDto(Post post) {
		PostDto postDto = this.modelMapper.map(post, PostDto.class);
		return postDto;
	}

	@Override
	public PostDto createPost(PostDto postDto, int categoryId, int userId) {
		User user = this.userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", "id", userId));
		
		Category category = this.categoryRepository.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", "id", categoryId));
		
		Post createdPost = this.dtoToPost(postDto);
		createdPost.setImageName("default.png");
		createdPost.setPostCreated(new Date());
		createdPost.setCategory(category);
		createdPost.setUser(user);
		this.postRepository.save(createdPost);
		
		return this.postToDto(createdPost);
	}

	@Override
	public PostDto updatePost(PostDto postDto, int id) {
		Post post = this.postRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepository.save(post);
		return this.postToDto(updatedPost);
	}

	@Override
	public PostDto getPostById(int id) {
		
		Post post = this.postRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
		return this.postToDto(post);
	}

	@Override
	public PostResponse getAllPost(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> pagePost = this.postRepository.findAll(p);
		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos =  posts.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setTotalRecords(pagePost.getTotalElements());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public void deletePost(int id) {
		Post post = this.postRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
		this.postRepository.delete(post);
		
	}

	@Override
	public List<PostDto> getPostByCategory(int categoryId) {
		
		Category category = this.categoryRepository.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", "id", categoryId));
		List<Post> posts = this.postRepository.findByCategory(category);
		List<PostDto> postDtos =  posts.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostByUser(int userId) {
		
		User user = this.userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", "id", userId));
		List<Post> posts = this.postRepository.findByUser(user);
		List<PostDto> postDtos =  posts.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> searchPost(String keyWord) {
		List<Post> posts = this.postRepository.findByTitleContaining(keyWord);
//		List<Post> posts = this.postRepository.searchPost("%" + keyWord + "%");
		List<PostDto> postDtos =  posts.stream().map(post -> this.postToDto(post)).collect(Collectors.toList());
		return postDtos;
	}
}
