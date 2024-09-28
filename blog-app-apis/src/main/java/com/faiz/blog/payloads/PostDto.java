package com.faiz.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.faiz.blog.entities.Comment;

public class PostDto {
	
	private int postId;	
	private String title;
	private String content;
	private String imageName;
	private Date postCreated;
	private CategoryDto category;
	private UserDto user;
	private Set<CommentDto> comments = new HashSet<>();
	
	public PostDto() {
		super();
	}

	public PostDto(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	
	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Date getPostCreated() {
		return postCreated;
	}

	public void setPostCreated(Date postCreated) {
		this.postCreated = postCreated;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<CommentDto> getComments() {
		return comments;
	}

	public void setComments(Set<CommentDto> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "PostDto [postId=" + postId + ", title=" + title + ", content=" + content + ", imageName=" + imageName
				+ ", postCreated=" + postCreated + ", category=" + category + ", user=" + user + ", comment=" + comments
				+ "]";
	}

}
