package com.example.rentalaccommodation.services;

import com.example.rentalaccommodation.DTOs.LoginDTO;
import com.example.rentalaccommodation.DTOs.ResultDTO;
import com.example.rentalaccommodation.DTOs.UserDTO;
import com.example.rentalaccommodation.authentications.UserPrincipal;

public interface UserService {
    ResultDTO create(UserDTO dto);

    ResultDTO getUserById(Long id, UserPrincipal currentUser);

    ResultDTO login(LoginDTO loginDTO);

    ResultDTO update(UserDTO dto, Long id, UserPrincipal currentUser);

    ResultDTO search(Integer page, Integer size, UserPrincipal currentUser);

    ResultDTO delete(Long id, UserPrincipal currentUser);
}
