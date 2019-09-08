package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Friendship;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.FriendshipRepository;
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
import java.util.*;

@RestController
@RequestMapping("/api/friendship")
@Api(description = "Точка входа для распознования")
public class FriendshipController {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private FriendshipRepository friendshipRepository;

    /**
     * Get all users list.
     *
     * @return the list
     */
    @Autowired
    public FriendshipController(UserRepository userRepository,

                                FriendshipRepository friendshipRepository,
                                PostRepository postRepository
    ) {
        this.userRepository = userRepository;
       this.friendshipRepository = friendshipRepository;
       this.postRepository = postRepository;
    }



    @GetMapping()
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<?> getAllFriendsOfCurrentUser(Authentication authentication) {
        List<Friendship> friendships =friendshipRepository.findAllByDeletedAtNullAndUserOne_Id(userRepository.findByUsername(authentication.getName()).getId());
        List<User> friends = new ArrayList<>();
        for(Friendship f:friendships){
            friends.add(f.getUserTwo());
        }
        System.out.println(friends);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/posts")
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<?> getPostsOfAllFriendsOfCurrentUser(Authentication authentication) {
        List<Friendship> friendships =friendshipRepository.findAllByDeletedAtNullAndUserOne_Id(userRepository.findByUsername(authentication.getName()).getId());
        List<User> friends = new ArrayList<>();
        for(Friendship f:friendships){
            friends.add(f.getUserTwo());
        }
        List<Post> allPosts = new ArrayList<>();
        for(User friend: friends){
            allPosts.addAll(postRepository.findAllByDeletedAtNullAndUser_Id(friend.getId()));
        }
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }


    @GetMapping("/iffriends/{id}")
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<?> ifFriends(Authentication authentication,@PathVariable(value = "id") Long friendId) {
        Friendship f = friendshipRepository.findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(userRepository.findByUsername(authentication.getName()).getId(),friendId);

        System.out.println(f);
        return new ResponseEntity<>(f, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<?> getFriendshipByFriendId(Authentication authentication,@PathVariable(value = "id") Long friendId) {
        System.out.println(friendId);
        Long userOne = userRepository.findByUsername(authentication.getName()).getId();
        Long userTwo = friendId;
        return new ResponseEntity<>(friendshipRepository.findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(userOne,userTwo), HttpStatus.OK);
    }





    /**
     * Gets users by id.
     *
     * @param userId the user id
     * @return the users by id
     * @throws ResourceNotFoundException the resource not found exception
     */

    /**
     * Create user user.
     *
     * @return the user
     */
//    @PostMapping()
//    @ApiOperation("Регистрация пользователей")
//    public ResponseEntity<Friendship> createFriendship(@Valid @RequestBody Friendship friendship,Authentication authentication) {
//        friendship.setUserOne(userRepository.findByUsername(authentication.getName()));
//        return new ResponseEntity<>(friendshipRepository.save(friendship), HttpStatus.OK);
//    }


    /**
     * Update user response entity.
     *
     * @param userId the user id
     * @param userDetails the user details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    /**
     * Delete user map.
     *
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteFriendship(@PathVariable(value = "id") Long friendshipId) throws Exception {
        System.out.println(friendshipId);
        Friendship friendshipOne =
                friendshipRepository
                        .findById(friendshipId)
                        .orElseThrow(() -> new ResourceNotFoundException("Friendship not found on :: " + friendshipId));
        Friendship friendshipTwo =  friendshipRepository.findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(friendshipOne.getUserTwo().getId(),friendshipOne.getUserOne().getId());
        friendshipTwo.setDeletedAt(new Date());
        friendshipOne.setDeletedAt(new Date());
        friendshipRepository.save(friendshipOne);
        friendshipRepository.save(friendshipTwo);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}