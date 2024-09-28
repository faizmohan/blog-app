package com.faiz.blog.services;

import com.faiz.blog.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto, int postId);
	void deleteComment(int commentId);
}
