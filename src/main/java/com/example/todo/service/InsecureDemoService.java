package com.example.todo.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service
public class InsecureDemoService {
    public String findUserByName(String username) throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM users WHERE name = '" + username + "'"
            );
            return resultSet.next() ? resultSet.getString(1) : null;
        }
    }

    public int runUserCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        return process.waitFor();
    }

    public String readFileFromUserInput(String baseDir, String userPath) throws IOException {
        Path path = Path.of(baseDir, userPath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public Object unsafeDeserialize(byte[] payload) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream =
                 new ObjectInputStream(new ByteArrayInputStream(payload))) {
            return objectInputStream.readObject();
        }
    }

    public String hardcodedCredentials() {
        String username = "admin";
        String password = "P@ssw0rd!";
        return username + ":" + password;
    }
}
