package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.Comment;
import com.okta.springbootvue.SpringBootVueApplication.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByDeletedAtNull();
    List<Comment> findAllByDeletedAtNullAndPost_id(Long postId);



}