package com.okta.springbootvue.SpringBootVueApplication.repositories;

import com.okta.springbootvue.SpringBootVueApplication.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RolesRepository extends JpaRepository<Roles,Long> {
}
