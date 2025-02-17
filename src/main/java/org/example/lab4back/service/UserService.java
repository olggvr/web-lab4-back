package org.example.lab4back.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.dao.UserDAO;
import org.example.lab4back.dto.UserInfoDTO;
import org.example.lab4back.exceptions.UserNotFoundException;

@Stateless
@Slf4j
public class UserService {
    @EJB
    private UserDAO userDAO;

    public UserInfoDTO getUserInfo(Long userId) throws UserNotFoundException {
        return userDAO.getUserInfo(userId);
    }

    public UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfo) throws UserNotFoundException {
        return userDAO.updateUserInfo(userId, userInfo);
    }
}
