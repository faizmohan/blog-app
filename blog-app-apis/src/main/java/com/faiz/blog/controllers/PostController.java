package com.faiz.blog.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.faiz.blog.payloads.ApiResponse;
import com.faiz.blog.payloads.PostDto;
import com.faiz.blog.payloads.PostResponse;
import com.faiz.blog.services.FileService;
import com.faiz.blog.services.PostService;
import com.faiz.blog.config.AppConstants;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileservice;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable int categoryId, @PathVariable int userId){
		PostDto createdPostDto =  this.postService.createPost(postDto, categoryId, userId);
		return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByCategory(@PathVariable int categoryId){
		List<PostDto> postDtos = this.postService.getPostByCategory(categoryId);
		return new ResponseEntity<>(postDtos, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByUser(@PathVariable int userId){
		List<PostDto> postDtos = this.postService.getPostByUser(userId);
		return new ResponseEntity<>(postDtos, HttpStatus.OK);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable int postId){
		return new ResponseEntity<>(this.postService.getPostById(postId),HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
	         @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize, @RequestParam (value = "sortBy", 
	         defaultValue = AppConstants.SORT_BY, required = false) String sortBy, @RequestParam (value = "sortDir", 
	         defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
//	pageSize = number of records to be shown on a page. pageNumber = On which page no. the record to be shown
	{
		return new ResponseEntity<>(this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable int postId){
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", true),HttpStatus.OK);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable int postId){
		PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<>(updatedPostDto,HttpStatus.OK);
	}
	
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keyword") String keyword){
		List<PostDto> result = this.postService.searchPost(keyword);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable int postId){
		String fileName = null;
		PostDto postDto = this.postService.getPostById(postId);
		try {
			fileName = this.fileservice.uploadImage(path, image);
			System.out.println(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse httpServletResponse) {
		
		try {
			InputStream resource = this.fileservice.getResource(path, imageName);
			httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
			try {
				StreamUtils.copy(resource, httpServletResponse.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
