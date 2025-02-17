package org.example.lab4back.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.lab4back.exceptions.ServerException;

import java.time.Duration;

@Path("/auth")
@Slf4j
public class AuthController {
    private static final NewCookie.Builder COOKIE = new NewCookie.Builder("token")
            .maxAge((int) Duration.ofMinutes(30).toSeconds())
            .path("/")
            .httpOnly(true);

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid UserDTO userDto) {
        try {
            var token = authService.registerUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
            var cookie = COOKIE.value(token).build();
            return Response.ok().cookie(cookie).build();
        } catch (UserExistsException | InvalidEmailException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (ServerException | UserNotFoundException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }
}
