package com.okta.springbootvue.SpringBootVueApplication.repositories;


import com.okta.springbootvue.SpringBootVueApplication.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticableUserRepository extends CrudRepository<User,Long> {
    @Query("select a from User a where a.username = :username")
    User findByUsername(@Param("username") String username);
}

