package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Like;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.LikesRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.PostRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.RolesRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@Api(description = "Точка входа для распознования")
public class LikesController {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private LikesRepository likesRepository;

    @Autowired
    public LikesController(UserRepository userRepository,
                           PostRepository postRepository,
                           LikesRepository likesRepository
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
    }



    /**
     * Get all posts list.
     *
     * @return the list
     */
    @GetMapping("{id}")
    @ApiOperation("")
    public ResponseEntity<?> getAllLikesOfPost(@PathVariable(value = "id") Long postId) {
        Post post = postRepository.getOne(postId);
        List<Like> likes = likesRepository.findAllByDeletedAtNullAndPost_id(post.getId());
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
    /**
     * Gets posts by id.
     *
     * @param postId the post id
     * @return the posts by id
     * @throws ResourceNotFoundException the resource not found exception
     */

    @GetMapping("getone/{id}")
    @ApiOperation("")
    public ResponseEntity<?> getLikeById(@PathVariable(value = "id") Long likeId) {
        return new ResponseEntity<>(likesRepository.findById(likeId), HttpStatus.OK);
    }
    /**
     * Create post post.
     *
     * @param post the post
     * @return the post
     */
    @PostMapping()
    @ApiOperation("")
    public ResponseEntity<?> createLike(@Valid @RequestBody Like like, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        like.setUser(user);
        List<Like> likes =  likesRepository.findAllByDeletedAtNullAndPost_id(like.getPost().getId());
        for(Like l:likes){
            if(l.getUser().getId()==user.getId()){
                l.setDeletedAt(new Date());
                likesRepository.save(l);
                System.out.println("Already exists");
                return new ResponseEntity<>("Already exists",HttpStatus.OK);

            }
        }

        return new ResponseEntity<>(likesRepository.save(like), HttpStatus.OK);
    }


    /**
     * Update post response entity.
     *
     * @param postId the post id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLike(
            @PathVariable(value = "id") Long likeId)
            throws ResourceNotFoundException {
       Like like = likesRepository.getOne(likeId);
       like.setDeletedAt(null);
        final Like updatedLike = likesRepository.save(like);
        return ResponseEntity.ok(updatedLike);
    }
    /**
     * Delete post map.
     *
     * @param postId the post id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteLike(@PathVariable(value = "id") Long likeId) throws Exception {
        Like like =
                likesRepository
                        .findById(likeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found on :: " + likeId));
        like.setDeletedAt(new Date());
        likesRepository.save(like);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}


