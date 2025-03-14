package org.example.lab4back.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.auth.UserPrincipal;
import org.example.lab4back.dto.ErrorDTO;
import org.example.lab4back.dto.PointDTO;
import org.example.lab4back.exceptions.PointNotFoundException;
import org.example.lab4back.exceptions.UserNotFoundException;
import org.example.lab4back.service.PointService;

import java.util.List;

@Path("/points")
@Slf4j
public class PointController {
    @Inject
    private PointService pointService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints() {
        try {
            var points = pointService.getPoints();
            return Response.ok(points).build();
        } catch (Exception e) {
            log.error("Error while getting points", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @GET
    @Path("/self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints() {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            List<PointDTO> points = pointService.getUserPoints(userPrincipal.getUserId());
            return Response.ok(points).build();
        } catch (UserNotFoundException e) {
            log.error("Error retrieving points for user {}: {}", userPrincipal.getUserId(), e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (Exception e) {
            log.error("Internal server error while retrieving points for user {}: {}", userPrincipal.getUserId(),
                    e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error."))
                    .build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserPoint(PointDTO pointDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            PointDTO createdPoint = pointService.addUserPoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok(createdPoint).build();
        } catch (Exception e) {
            log.error("Error adding point for user {}: {}", userPrincipal.getUserId(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserPoint() {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            Long userId = userPrincipal.getUserId();
            pointService.deleteUserPoints(userId);
            return Response.ok().entity(ErrorDTO.of("All points deleted successfully.")).build();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of("User not found")).build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error"))
                    .build();
        }
    }

    @PATCH
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePoint(PointDTO pointDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            pointService.deleteSinglePoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok().entity(ErrorDTO.of("Point deleted successfully.")).build();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of("User not found")).build();
        } catch (PointNotFoundException e) {
            log.error("Point not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of("Point not found")).build();
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error"))
                    .build();
        }
    }
}
