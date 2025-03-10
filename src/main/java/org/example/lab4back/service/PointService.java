package org.example.lab4back.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.dao.PointDAO;
import org.example.lab4back.dao.UserDAO;
import org.example.lab4back.dto.PointDTO;
import org.example.lab4back.entity.PointEntity;
import org.example.lab4back.entity.UserEntity;
import org.example.lab4back.exceptions.PointNotFoundException;
import org.example.lab4back.exceptions.UserNotFoundException;
import org.example.lab4back.utils.AreaChecker;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Slf4j
public class PointService {
    @EJB
    private UserDAO userDAO;

    @EJB
    private PointDAO pointDAO;

    public List<PointDTO> getPoints() {
        var points = pointDAO.getAll();
        return points.stream()
                .map(PointDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PointDTO> getUserPoints(Long userId) throws UserNotFoundException {
        List<PointEntity> points = pointDAO.getPointsByUserId(userId);
        return points.stream()
                .map(PointDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PointDTO addUserPoint(Long userId, PointDTO pointDTO) throws UserNotFoundException {
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isInsideArea = AreaChecker.isInArea(pointDTO.getX(), pointDTO.getY(), pointDTO.getR());
        PointEntity pointEntity = PointEntity.builder()
                .x(pointDTO.getX())
                .y(pointDTO.getY())
                .r(pointDTO.getR())
                .result(isInsideArea)
                .user(user)
                .build();

        pointDAO.addPointByUserId(userId, pointEntity);
        return PointDTO.builder()
                .x(pointEntity.getX())
                .y(pointEntity.getY())
                .r(pointEntity.getR())
                .result(pointEntity.isResult())
                .build();
    }

    public void deleteUserPoints(Long userId) throws UserNotFoundException {
        pointDAO.removeAllPointsByUserId(userId);
    }

    public void deleteSinglePoint(Long userId, PointDTO pointDTO) throws UserNotFoundException, PointNotFoundException {
        pointDAO.removePointByUserId(userId, pointDTO);
    }

    public void updateLastActivity(Long userId) {
        userDAO.updateLastActivity(userId);
    }
}