package com.example.rentalaccommodation.services.impl;

import com.example.rentalaccommodation.DTOs.*;
import com.example.rentalaccommodation.authentications.TokenProvider;
import com.example.rentalaccommodation.authentications.TokenUser;
import com.example.rentalaccommodation.authentications.UserPrincipal;
import com.example.rentalaccommodation.constants.ResponseConstant;
import com.example.rentalaccommodation.enums.RoleEnum;
import com.example.rentalaccommodation.models.RefreshToken;
import com.example.rentalaccommodation.models.User;
import com.example.rentalaccommodation.repositories.RefreshTokenRepository;
import com.example.rentalaccommodation.repositories.UserRepository;
import com.example.rentalaccommodation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenProvider tokenProvider;

    @Value("${authentication.token.jwt.expire}")
    private int tokenTimeout;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public ResultDTO create(UserDTO dto) {
        User checkExistUser = userRepository.getUserByUsername(dto.getUsername());
        if (checkExistUser != null) {
            return new ResultDTO(ResponseConstant.RESPONSE_USER_EXIST_CODE, ResponseConstant.RESPONSE_USER_EXIST, null);
        }

        List<String> scopes = new ArrayList<>();
        scopes.add(RoleEnum.USER.toString());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setAvatar(dto.getAvatar());
        user.setScopes(scopes);
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
        return new ResultDTO(ResponseConstant.RESPONSE_CREATE_SUCCESS_CODE, ResponseConstant.RESPONSE_CREATE_SUCCESS, dto);
    }

    @Override
    public ResultDTO getUserById(Long id, UserPrincipal currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        System.out.println("User id: "+user.getId().toString());

        User user1 = userRepository.findUserById(id);
        UserSearchDTO userSearchDTO = new UserSearchDTO();
        if (user1 != null) {
            userSearchDTO.setId(user1.getId());
            userSearchDTO.setName(user1.getName());
            userSearchDTO.setEmail(user1.getEmail());
            userSearchDTO.setAvatar(user1.getAvatar());
            userSearchDTO.setUsername(user1.getUsername());
            userSearchDTO.setScopes(user1.getScopes());
            return new ResultDTO(ResponseConstant.RESPONSE_SEARCH_SUCCESS_CODE, ResponseConstant.RESPONSE_SEARCH_SUCCESS, userSearchDTO);
        }
        return new ResultDTO(ResponseConstant.RESPONSE_SEARCH_FAIL_CODE, ResponseConstant.RESPONSE_SEARCH_FAIL, null);
    }

    @Override
    public ResultDTO login(LoginDTO loginDTO) {
        User user = userRepository.getUserByUsername(loginDTO.getUsername());
        ResultDTO resultDTO = new ResultDTO();

        if (user == null) {
            return new ResultDTO(ResponseConstant.RESPONSE_USER_DO_NOT_EXIST_CODE, ResponseConstant.RESPONSE_USER_DO_NOT_EXIST, null);
        } else {
            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
                return new ResultDTO(ResponseConstant.RESPONSE_USER_PASSWORD_WRONG_CODE, ResponseConstant.RESPONSE_USER_PASSWORD_WRONG, null);

            TokenDTO tokenDTO = authorizeUser(user);
            resultDTO.setCode(ResponseConstant.RESPONSE_USER_LOGIN_SUCCESS_CODE);
            resultDTO.setDescription(ResponseConstant.RESPONSE_USER_LOGIN_SUCCESS);
            resultDTO.setData(tokenDTO);
        }
        return resultDTO;
    }

    @Override
    public ResultDTO update(UserDTO dto, Long id, UserPrincipal currentUser) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        User user = userRepository.findUserById(id);
        user.setUsername(dto.getUsername());
        user.setAvatar(dto.getAvatar());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);

        UserSearchDTO userSearchDTO = new UserSearchDTO();
        userSearchDTO.setUsername(user.getUsername());
        userSearchDTO.setName(user.getName());
        userSearchDTO.setEmail(user.getEmail());
        userSearchDTO.setAvatar(user.getAvatar());
        userSearchDTO.setId(user.getId());
        userSearchDTO.setScopes(user.getScopes());
        return new ResultDTO(ResponseConstant.RESPONSE_UPDATE_SUCCESS_CODE, ResponseConstant.RESPONSE_UPDATE_SUCCESS, userSearchDTO);
    }

    @Override
    public ResultDTO search(Integer page, Integer size, UserPrincipal currentUser) {
        userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Pageable pageable = PageRequest.of(page, size);
        Page<User> listData = userRepository.findAll(pageable);
        List<UserSearchDTO> listDataResponse = new ArrayList<>();
        for (User data : listData) {
            UserSearchDTO dataResponse = new UserSearchDTO();
            dataResponse.setName(data.getName());
            dataResponse.setUsername(data.getUsername());
            dataResponse.setId(data.getId());
            dataResponse.setAvatar(data.getAvatar());
            dataResponse.setScopes(data.getScopes());
            dataResponse.setEmail(data.getEmail());
            listDataResponse.add(dataResponse);

        }
        long totalPage = listData.getTotalPages();
        long totalSize = listData.getTotalElements();

        UserSearchListDTO userSearchListDTO = new UserSearchListDTO();
        userSearchListDTO.setListData(listDataResponse);
        userSearchListDTO.setTotalPage(totalPage);
        userSearchListDTO.setTotalSize(totalSize);

        return new ResultDTO(ResponseConstant.RESPONSE_SEARCH_SUCCESS_CODE, ResponseConstant.RESPONSE_SEARCH_SUCCESS, userSearchListDTO);
    }

    @Override
    public ResultDTO delete(Long id, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        User data = userRepository.findUserById(currentUser.getId());
        userRepository.delete(data);
        return new ResultDTO(ResponseConstant.RESPONSE_DELETE_SUCCESS_CODE, ResponseConstant.RESPONSE_DELETE_SUCCESS, null);
    }

    public TokenDTO authorizeUser(User user) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(user.getId());
        tokenUser.setEmail(user.getEmail());
        List<String> scopes = new ArrayList<>(user.getScopes());
        tokenUser.setAuthorities(scopes);
        tokenUser.setName(user.getName());

        UserPrincipal userPrincipal = UserPrincipal.create(tokenUser);
        String token = tokenProvider.issueToken(userPrincipal);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setName(userPrincipal.getName());
        tokenDTO.setExpire(LocalDateTime.now().plusSeconds(tokenTimeout).toEpochSecond(ZoneOffset.UTC));
        tokenDTO.setToken(token);
        tokenDTO.setBearerToken("Bearer " + token);

        RefreshToken refreshToken = new RefreshToken();
        //refreshToken.setUserId(user.getId());
        refreshToken.setCreatedDate(LocalDateTime.now());
        refreshToken.setUpdatedDate(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);

        tokenDTO.setRefreshToken(refreshToken.getId().toString());
        return tokenDTO;
    }
}
