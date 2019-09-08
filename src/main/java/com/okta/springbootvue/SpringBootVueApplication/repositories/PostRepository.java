package com.okta.springbootvue.SpringBootVueApplication.repositories;


import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByDeletedAtNull();
    List<Post> findAllByDeletedAtNullAndUser_Id(Long userId);
    Post findAllByDeletedAtNullAndId(Long id);



}
