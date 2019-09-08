package com.okta.springbootvue.SpringBootVueApplication.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="todos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="completed")
    private Boolean completed = false;

}