package org.example.lab4back.dao;

import org.example.lab4back.dto.PointDTO;
import org.example.lab4back.entity.PointEntity;
import org.example.lab4back.entity.UserEntity;
import org.example.lab4back.exceptions.PointNotFoundException;
import org.example.lab4back.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PointDAO {
    List<PointEntity> getAll();

    List<PointEntity> getPointsByUserId(Long userId) throws UserNotFoundException;

    void addPointByUserId(Long userId, PointEntity point) throws UserNotFoundException;

    void removePointByUserId(Long userId, PointDTO pointDTO) throws UserNotFoundException, PointNotFoundException;

    void removeAllPointsByUserId(Long userId) throws UserNotFoundException;

    Optional<UserEntity> findById(Long userId);
}