package com.example.rentalaccommodation.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String name;
    private Long expire;
    private String token;
    private String bearerToken;
    private List<String> scopes;
    private String refreshToken;
}
