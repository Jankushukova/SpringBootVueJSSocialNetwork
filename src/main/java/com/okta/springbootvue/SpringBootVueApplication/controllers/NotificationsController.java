package com.okta.springbootvue.SpringBootVueApplication.controllers;

import com.okta.springbootvue.SpringBootVueApplication.models.Friendship;
import com.okta.springbootvue.SpringBootVueApplication.models.Notification;
import com.okta.springbootvue.SpringBootVueApplication.models.Notification;
import com.okta.springbootvue.SpringBootVueApplication.models.Post;
import com.okta.springbootvue.SpringBootVueApplication.repositories.*;
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
@RequestMapping("/api/notifications")
@Api(description = "Точка входа для распознования")
public class NotificationsController {
    private UserRepository userRepository;
    private NotificationsRepository notificationsRepository;
    private FriendshipRepository friendshipRepository;

    @Autowired
    public NotificationsController(UserRepository userRepository,
                                   NotificationsRepository notificationsRepository,
                                   FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
        this.friendshipRepository = friendshipRepository;

    }



    /**
     * Get all posts list.
     *
     * @return the list
     */
    @GetMapping()
    @ApiOperation("")
    public ResponseEntity<?> getAllNotificationsOfCurrentUser(Authentication authentication) {

        return new ResponseEntity<>(notificationsRepository.findAllByDeletedAtNullAndUserTwo_Id(userRepository.findByUsername(authentication.getName()).getId()), HttpStatus.OK);
    }

    @GetMapping("/ifaccepted/{id}")
    @ApiOperation("")
    public ResponseEntity<?> ifAccepted(@PathVariable(value = "id") Long userTwo,Authentication authentication) {
        Notification note = notificationsRepository.findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(userRepository.findByUsername(authentication.getName()).getId(),userTwo);
        if(note!=null){
            System.out.println(note.getUserOne().getFullname());
            System.out.println(note.getUserTwo().getFullname());
        }
        System.out.println(note);

        return new ResponseEntity<>(note, HttpStatus.OK);
    }
    /**
     * Gets posts by id.
     *
     * @return the posts by id
     * @throws ResourceNotFoundException the resource not found exception
     */


    @PostMapping()
    @ApiOperation("")
    public ResponseEntity<?> createNotification(@Valid @RequestBody Notification notification, Authentication authentication) {

        notification.setUserOne(userRepository.findByUsername(authentication.getName()));
        return new ResponseEntity<>(notificationsRepository.save(notification), HttpStatus.OK);
    }


    /**
     * Update post response entity.
     *
     * @param notificationId the post id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */

    /**
     * Delete post map.
     *
     * @param notificationId the post id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("/notaccept/{id}")
    public Map<String, Boolean> DoNotAcceptAndDeleteNotification(@PathVariable(value = "id") Long notificationId) throws Exception {
        System.out.println("do not accept");
        Notification notification =
                notificationsRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Notification not found on :: " + notificationId));
        notification.setDeletedAt(new Date());
        notificationsRepository.save(notification);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/accept/{id}")
    public Map<String, Boolean> AcceptAndDeleteNotification(@PathVariable(value = "id") Long notificationId) throws Exception {
        System.out.println("accept");

        Notification notification =
                notificationsRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Notification not found on :: " + notificationId));
        Friendship friendshipOne = Friendship.builder().build();
        friendshipOne.setUserOne(notification.getUserOne());
        friendshipOne.setUserTwo(notification.getUserTwo());
        friendshipOne.setCreatedAt(new Date());
        friendshipOne.setUpdatedAt(new Date());
        friendshipRepository.save(friendshipOne);
        Friendship friendshipTwo = Friendship.builder().build();
        friendshipTwo.setUserTwo(notification.getUserOne());
        friendshipTwo.setUserOne(notification.getUserTwo());
        friendshipTwo.setCreatedAt(new Date());
        friendshipTwo.setUpdatedAt(new Date());
        friendshipRepository.save(friendshipTwo);

        notification.setDeletedAt(new Date());
        notificationsRepository.save(notification);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}


