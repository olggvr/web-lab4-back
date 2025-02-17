package org.example.lab4back.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.auth.UserPrincipal;
import org.example.lab4back.dto.ErrorDTO;
import org.example.lab4back.dto.UserInfoDTO;
import org.example.lab4back.exceptions.UserNotFoundException;
import org.example.lab4back.service.UserService;

@Path("/users")
@Slf4j
public class UserController {
    @Inject
    private UserService userService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo() {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var user = userService.getUserInfo(userPrincipal.getUserId());
            return Response.ok(user).build();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (Exception e) {
            log.error("Error retrieving user info: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserInfo(@Valid UserInfoDTO userInfo) {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var user = userService.updateUserInfo(userPrincipal.getUserId(), userInfo);
            return Response.ok(user).build();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (Exception e) {
            log.error("Error updating user info: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }
}
