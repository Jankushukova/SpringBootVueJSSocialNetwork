package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.Friendship;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    Friendship findAllByDeletedAtNullAndUserOne_IdAndUserTwo_Id(Long userOne,Long userTwo);

    

    List<Friendship> findAllByDeletedAtNullAndUserOne_Id(Long id);
}