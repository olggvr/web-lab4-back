package org.example.lab4back.exceptions;

public class PointNotFoundException extends RuntimeException {
    public PointNotFoundException(String message) {
        super(message);
    }
}
