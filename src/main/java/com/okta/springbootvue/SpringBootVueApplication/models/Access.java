package com.okta.springbootvue.SpringBootVueApplication.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "accesses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Access{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="access_user",nullable = false)
    private User access_user;

    @CreationTimestamp
    private Date data;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

}
