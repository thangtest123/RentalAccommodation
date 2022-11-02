package com.example.rentalaccommodation.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class UserSearchListDTO {
    List<UserSearchDTO> listData;
    long totalPage;
    long totalSize;
}
