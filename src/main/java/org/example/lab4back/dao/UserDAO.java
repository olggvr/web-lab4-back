package org.example.lab4back.dao;

import org.example.lab4back.dto.UserInfoDTO;
import org.example.lab4back.entity.UserEntity;
import org.example.lab4back.exceptions.ServerException;
import org.example.lab4back.exceptions.UserNotFoundException;

import java.util.Optional;

public interface UserDAO {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long userId);

    UserEntity createUser(UserEntity user) throws ServerException;

    void startNewSession(Long userId) throws UserNotFoundException;

    void endSession(Long userId) throws UserNotFoundException;

    void updateLastActivity(Long userId);

    Optional<UserEntity> findByEmail(String email);

    UserInfoDTO getUserInfo(Long userId) throws UserNotFoundException;

    UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfo) throws UserNotFoundException;
}