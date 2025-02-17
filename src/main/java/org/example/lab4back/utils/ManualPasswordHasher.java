package org.example.lab4back.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ManualPasswordHasher {

    public static void main(String[] args) {
        String password = "example";
        int cost = 12;
        String hashedPassword = BCrypt.withDefaults().hashToString(cost, password.toCharArray());
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
