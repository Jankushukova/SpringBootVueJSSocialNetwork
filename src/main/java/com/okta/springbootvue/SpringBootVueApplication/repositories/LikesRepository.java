package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.Like;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Like,Long> {
    List<Like> findAllByDeletedAtNull();
    List<Like> findAllByDeletedAtNullAndPost_id(Long postId);


}
