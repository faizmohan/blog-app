package com.faiz.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faiz.blog.entities.Comment;
import com.faiz.blog.entities.Post;
import com.faiz.blog.exceptions.ResourceNotFoundException;
import com.faiz.blog.payloads.CommentDto;
import com.faiz.blog.repositories.CommentRepository;
import com.faiz.blog.repositories.PostRepository;
import com.faiz.blog.services.CommentService;

@Service

public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, int postId) {
		
		Post post = this.postRepository.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", "id", postId));
		
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		
		comment.setPost(post);
		
		Comment savedComment = this.commentRepo.save(comment);
		
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(int commentId) {
		
		Comment comment = this.commentRepo.findById(commentId).orElseThrow( () -> new ResourceNotFoundException("Comment", "id", commentId));
		
		this.commentRepo.delete(comment);
	}

}
