package com.example.rentalaccommodation.authentications;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TokenUser {
    private Long id;
    private String name;
    private String email;
    private Integer expire;
    private List<String> authorities;
}
