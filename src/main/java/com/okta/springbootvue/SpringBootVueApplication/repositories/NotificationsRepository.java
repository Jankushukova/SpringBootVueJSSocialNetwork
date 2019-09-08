package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.Comment;
import com.okta.springbootvue.SpringBootVueApplication.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByDeletedAtNullAndUserOne_Id(Long id);
    List<Notification> findAllByDeletedAtNullAndUserTwo_Id(Long id);
    Notification findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(Long userIne,Long userTwo);
}