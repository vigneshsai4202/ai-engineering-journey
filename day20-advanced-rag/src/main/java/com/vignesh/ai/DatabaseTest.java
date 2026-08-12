package com.vignesh.ai;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        String url =
                "jdbc:postgresql://"
                        + dotenv.get("NEON_HOST")
                        + ":"
                        + dotenv.get("NEON_PORT")
                        + "/"
                        + dotenv.get("NEON_DATABASE");

        try (Connection connection =
                     DriverManager.getConnection(
                             url,
                             dotenv.get("NEON_USERNAME"),
                             dotenv.get("NEON_PASSWORD"))) {

            System.out.println(
                    "✅ Connected to Neon PostgreSQL!"
            );

            System.out.println(
                    "Database: "
                            + connection.getCatalog()
            );

        } catch (Exception e) {

            System.out.println(
                    "❌ Database connection failed!"
            );

            e.printStackTrace();
        }
    }
}