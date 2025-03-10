package org.example.lab4back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lab4back.entity.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be alphanumeric with underscores")
    private String username;

    @Size(min = 3, max = 20, message = "Email must be between 3 and 20 characters")
    @Email(message = "Invalid email format")
    private String email;

    private String avatarUrl;

    public static UserInfoDTO fromEntity(UserEntity entity) {
        return UserInfoDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }
}
