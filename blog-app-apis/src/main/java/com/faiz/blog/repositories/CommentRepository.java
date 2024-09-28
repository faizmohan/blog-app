package com.faiz.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faiz.blog.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
