package com.example.rentalaccommodation.controllers;

import com.example.rentalaccommodation.DTOs.LoginDTO;
import com.example.rentalaccommodation.DTOs.ResultDTO;
import com.example.rentalaccommodation.DTOs.UserDTO;
import com.example.rentalaccommodation.authentications.UserPrincipal;
import com.example.rentalaccommodation.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
@SecurityRequirement(name = "bearer-key")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResultDTO> create(@RequestBody UserDTO dto) {
        return new ResponseEntity<ResultDTO>(userService.create(dto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResultDTO> login(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<ResultDTO>(userService.login(loginDTO), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ResultDTO> getbyUserId(@PathVariable(name = "id") Long id,
                                                 @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        return new ResponseEntity<ResultDTO>(userService.getUserById(id, currentUser), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResultDTO> update(@RequestBody UserDTO dto,
                                            @PathVariable(name = "id") Long id,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        return new ResponseEntity<ResultDTO>(userService.update(dto, id, currentUser), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ResultDTO> search(@RequestParam(name = "page") Integer page,
                                            @RequestParam(name = "pageSize") Integer pageSize,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        return new ResponseEntity<ResultDTO>(userService.search(page, pageSize, currentUser), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultDTO> delete(@PathVariable(name = "id") Long id,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        return new ResponseEntity<ResultDTO>(userService.delete(id, currentUser), HttpStatus.OK);
    }
}
