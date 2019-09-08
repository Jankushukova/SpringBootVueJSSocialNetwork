package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.PostRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.RolesRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
@Api(description = "Точка входа для распознования")
public class PostsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * Get all posts list.
     *
     * @return the list
     */
    @GetMapping()
    @ApiOperation("")
    public ResponseEntity<?> getAllPostsOfCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Long post_user = user.getId();
        return new ResponseEntity<>(postRepository.findAllByDeletedAtNullAndUser_Id(post_user), HttpStatus.OK);
    }


    @GetMapping("/custom/{id}")
    @ApiOperation("")
    public ResponseEntity<?> getAllPostsOfCustomUser(@PathVariable(value = "id") Long userId) {
        User user = userRepository.getOne(userId);
        Long post_user = user.getId();
        return new ResponseEntity<>(postRepository.findAllByDeletedAtNullAndUser_Id(post_user), HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    @ApiOperation("")
    public ResponseEntity<?> getUserOfPost(@PathVariable(value = "id") Long postId) {
        User post_user = postRepository.findAllByDeletedAtNullAndId(postId).getUser();
        return new ResponseEntity<>(post_user, HttpStatus.OK);
    }
    /**
     * Gets posts by id.
     *
     * @param postId the post id
     * @return the posts by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("{id}")
    @ApiOperation("Получение по ID")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") Long postId)
            throws ResourceNotFoundException {
        System.out.println("in post get by id");
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found on :: " + postId));
        return ResponseEntity.ok().body(post);
    }
    /**
     * Create post post.
     *
     * @param post the post
     * @return the post
     */
    @PostMapping()
    @ApiOperation("")
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post,Authentication authentication) {
        post.setUser(userRepository.findByUsername(authentication.getName()));
        return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
    }


    /**
     * Update post response entity.
     *
     * @param postId the post id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable(value = "id") Long postId, @Valid @RequestBody Post newPost)
            throws ResourceNotFoundException {
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found on :: " + postId));
        post.setTitle(newPost.getTitle());
        post.setText(newPost.getText());
        System.out.println("inpost update");
        final Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);
    }
    /**
     * Delete post map.
     *
     * @param postId the post id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("{id}")
    public Map<String, Boolean> deletePost(@PathVariable(value = "id") Long postId) throws Exception {
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found on :: " + postId));
        post.setDeletedAt(new Date());
        System.out.println(post.getDeletedAt());
        postRepository.save(post);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}