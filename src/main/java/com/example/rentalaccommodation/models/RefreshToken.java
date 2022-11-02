package com.example.rentalaccommodation.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    //private Long userId;

    @Column(name = "createdDate")
    @CreatedDate
    LocalDateTime createdDate;

    @Column(name = "updatedDate")
    @LastModifiedDate
    LocalDateTime updatedDate;
}
