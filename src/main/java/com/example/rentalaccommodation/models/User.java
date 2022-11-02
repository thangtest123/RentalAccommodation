package com.example.rentalaccommodation.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "tbl_user")
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @ElementCollection(targetClass=String.class)
    @Column(name = "scopes")
    private List<String> scopes;

    @CreatedDate
    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updatedDate")
    private LocalDateTime updatedDate;
}
