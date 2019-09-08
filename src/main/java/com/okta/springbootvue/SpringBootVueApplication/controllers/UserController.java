package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Friendship;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.FriendshipRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.RolesRepository;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import com.okta.springbootvue.SpringBootVueApplication.security.CustomUserDetailsService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Api(description = "Точка входа для распознования")
public class UserController {
    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    private FriendshipRepository friendshipRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * Get all users list.
     *
     * @return the list
     */
    @Autowired
    public UserController(UserRepository userRepository,
                           RolesRepository rolesRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                          FriendshipRepository friendshipRepository
                          ) {
       this.userRepository = userRepository;
       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
       this.rolesRepository = rolesRepository;
       this.friendshipRepository = friendshipRepository;
    }



    @GetMapping()
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) {
        System.out.println(authentication.getName());
        System.out.println("sign up");
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/just")
    @ApiOperation("Получение всех пользователей")
    public ResponseEntity<List<User>> getJust() {
        System.out.println("sign up");
        return null;
    }


    /**
     * Gets users by id.
     *
     * @param userId the user id
     * @return the users by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("{id}")
    @ApiOperation("Получение по ID")
    public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        return ResponseEntity.ok().body(user);
    }
    /**
     * Create user user.
     *
     * @param user the user
     * @return the user
     */
    @PostMapping("/register")
    @ApiOperation("Регистрация пользователей")
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        System.out.println("register");
        System.out.println(user.getFullname());
        user.setRole(rolesRepository.getOne(new Long(2)));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

    @PostMapping("/validate")
    @ApiOperation("Валидация логина")
    public ResponseEntity<String> validate(@RequestParam String login) {
        User user = userRepository.findByUsername(login);
        if (user != null) {
            System.out.println("login exists");
            return new ResponseEntity<>("Invalid login", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<  >("Valid login", HttpStatus.OK);
        }

    }
    /**
     * Update user response entity.
     *
     * @param userId the user id
     * @param userDetails the user details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails)
            throws ResourceNotFoundException {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setFullname(userDetails.getFullname());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
    /**
     * Delete user map.
     *
     * @param userId the user id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        System.out.println("current user");
        String login = authentication.getName();
        System.out.println(login);
        User user = userRepository.findByUsername(login);
        if(user==null){
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/not")
    @ApiOperation("Получение всех не друзей")
    public ResponseEntity<?> getNotFriendsOfCurrentUser(Authentication authentication) {
        System.out.println("in not friends");
        List<Friendship> friendships = friendshipRepository.findAllByDeletedAtNullAndUserOne_Id(userRepository.findByUsername(authentication.getName()).getId());
        List<Long> friendsId = new ArrayList<>();
        for(Friendship f:friendships){

            System.out.println("ol");
            friendsId.add(f.getUserTwo().getId());
        }
        System.out.println(friendsId.size());
        if(friendsId.size()==0){
            return new ResponseEntity<>(userRepository.findAllByDeletedAtNullAndRole_IdNotAndIdNot((long)1,userRepository.findByUsername(authentication.getName()).getId()), HttpStatus.OK);
        }
        System.out.println(userRepository.findAllByDeletedAtNullAndIdNotInAndRole_idNotAndIdNot(friendsId,(long)1,userRepository.findByUsername(authentication.getName()).getId()));
        return new ResponseEntity<>(userRepository.findAllByDeletedAtNullAndIdNotInAndRole_idNotAndIdNot(friendsId,(long)1, userRepository.findByUsername(authentication.getName()).getId()), HttpStatus.OK);
    }
}