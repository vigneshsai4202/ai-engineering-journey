package com.vignesh.ai;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        String host = dotenv.get("NEON_HOST");
        String port = dotenv.get("NEON_PORT");
        String database = dotenv.get("NEON_DATABASE");
        String username = dotenv.get("NEON_USERNAME");
        String password = dotenv.get("NEON_PASSWORD");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database
                + "?sslmode=require";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            System.out.println("✅ Connected to Neon PostgreSQL!");
            System.out.println("Database: " + connection.getCatalog());

        } catch (Exception e) {

            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
}