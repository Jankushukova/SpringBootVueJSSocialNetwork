package com.okta.springbootvue.SpringBootVueApplication.models;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@SequenceGenerator(
//        name = "seq",
//        sequenceName = "s_roles",
//        initialValue = 1,
//        allocationSize = 1
//)
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(unique = true)
    private String name ;

}
