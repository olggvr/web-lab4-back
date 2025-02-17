package org.example.lab4back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private String message;

    public static ErrorDTO of(String message) {
        return new ErrorDTO(message);
    }
}
