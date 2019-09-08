package com.okta.springbootvue.SpringBootVueApplication.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="title")
    private String title;


    @Column(name="text")
    private String text;

    @CreationTimestamp
    private Date data;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user",nullable = false)
    private User user;
//
//    @OneToMany(mappedBy="comment_post")
//    private List<Comment> comments;
//
//
//    @OneToMany(mappedBy="like_post")
//    private List<Like> likes;



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;



}
