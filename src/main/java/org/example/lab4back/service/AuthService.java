package org.example.lab4back.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.auth.JwtProvider;
import org.example.lab4back.auth.PasswordHasher;
import org.example.lab4back.dao.UserDAO;
import org.example.lab4back.entity.UserEntity;
import org.example.lab4back.enums.Role;
import org.example.lab4back.exceptions.*;

import java.util.Optional;

@Stateless
@Slf4j
public class AuthService {
    @EJB
    private UserDAO userDAO;

    @Inject
    private JwtProvider jwtProvider;

    public String registerUser(String username, String password, String email) throws UserExistsException, ServerException, UserNotFoundException, InvalidEmailException {
        if (userDAO.findByUsername(username).isPresent()) {
            throw new UserExistsException("User already exists: " + username);
        }

        UserEntity newUser = UserEntity.builder().username(username).email(email).password(PasswordHasher.hashPassword(password.toCharArray())).role(Role.USER).build();

        UserEntity createdUser = userDAO.createUser(newUser);

        log.info("Successfully added user: {}", createdUser);

        String token = jwtProvider.generateToken(createdUser.getUsername(), Role.USER, createdUser.getId(), createdUser.getEmail());
        userDAO.startNewSession(jwtProvider.getUserIdFromToken(token));

        return token;
    }

    public String authenticateUser(String email, String password) throws AuthenticationException, ServerException, UserNotFoundException {
        Optional<UserEntity> userOpt = userDAO.findByEmail(email);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (PasswordHasher.checkPassword(password.toCharArray(), user.getPassword())) {
                String token = jwtProvider.generateToken(user.getUsername(), Role.USER, user.getId(), user.getEmail());
                userDAO.startNewSession(jwtProvider.getUserIdFromToken(token));
                return token;
            } else {
                throw new AuthenticationException("Password is incorrect");
            }
        }
        throw new AuthenticationException("There is no user with this email");
    }

    public void endSession(Long userId) throws UserNotFoundException {
        userDAO.endSession(userId);
    }
}
