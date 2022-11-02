package com.example.rentalaccommodation.DTOs;

import lombok.Data;

@Data
public class UserDTO {
    private String username;

    private String password;

    private String name;

    private String email;

    private String avatar;
}
