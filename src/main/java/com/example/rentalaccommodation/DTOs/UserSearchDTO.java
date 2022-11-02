package com.example.rentalaccommodation.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class UserSearchDTO {
    private Long id;

    private String username;

    private String name;

    private String email;

    private String avatar;

    private List<String> scopes;
}
