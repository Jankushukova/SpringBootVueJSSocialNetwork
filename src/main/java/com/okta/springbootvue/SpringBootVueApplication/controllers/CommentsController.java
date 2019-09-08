package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Comment;
import com.okta.springbootvue.SpringBootVueApplication.models.Like;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.CommentRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.LikesRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.PostRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@Api(description = "Точка входа для распознования")
public class CommentsController {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentsController(UserRepository userRepository,
                           PostRepository postRepository,
                              CommentRepository commentRepository
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }



    /**
     * Get all posts list.
     *
     * @return the list
     */
    @GetMapping("{id}")
    @ApiOperation("")
    public ResponseEntity<?> getAllCommentsOfPost(@PathVariable(value = "id") Long postId) {

        Post post = postRepository.getOne(postId);
        List<Comment> comments = commentRepository.findAllByDeletedAtNullAndPost_id(post.getId());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    /**
     * Gets posts by id.
     *
     * @param commentId the post id
     * @return the posts by id
     * @throws ResourceNotFoundException the resource not found exception
     */

    @GetMapping("getone/{id}")
    @ApiOperation("")
    public ResponseEntity<?> getCommentById(@PathVariable(value = "id") Long commentId) {
        return new ResponseEntity<>(commentRepository.findById(commentId), HttpStatus.OK);
    }
    /**
     * Create post post.
     *
     * @param comment the post
     * @return the post
     */
    @PostMapping()
    @ApiOperation("")
    public ResponseEntity<?> createComment(@Valid @RequestBody Comment comment,Authentication authentication) {
        comment.setUser(userRepository.findByUsername(authentication.getName()));
        System.out.println("create comment");
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
    }


    /**
     * Update post response entity.
     *
     * @param commentId the post id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable(value = "id") Long commentId, @Valid @RequestBody Comment newComment)
            throws ResourceNotFoundException {
        Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found on :: " + commentId));
        comment.setDescription(newComment.getDescription());
        comment.setPost(newComment.getPost());
        comment.setUser(newComment.getUser());
        System.out.println("incomment update");
        final Comment updatedComment = commentRepository.save(comment);
        return ResponseEntity.ok(updatedComment);
    }
    /**
     * Delete post map.
     *
     * @param commentId the post id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteComment(@PathVariable(value = "id") Long commentId) throws Exception {
        Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found on :: " + commentId));
        comment.setDeletedAt(new Date());
        commentRepository.save(comment);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}


