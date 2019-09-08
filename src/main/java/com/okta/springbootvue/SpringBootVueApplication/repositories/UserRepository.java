package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select a from User a where a.username = :username")
    User findByUsername(String username);

    List<User> findAllByDeletedAtNullAndIdNotInAndRole_idNotAndIdNot(List<Long> ids,Long id,Long userId);

    List<User> findAllByDeletedAtNullAndRole_IdNotAndIdNot(Long id,Long UserId);

}
