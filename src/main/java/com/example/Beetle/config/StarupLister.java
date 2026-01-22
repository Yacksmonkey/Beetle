package com.example.Beetle.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class StarupLister {

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        checkDatabaseConnection();
        System.out.println("Server is ready on port");
        System.out.println("http://localhost:8080");
    }

    private void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database");
                System.out.println("User: " + connection.getMetaData().getUserName());
                System.out.println("Running query...");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database");
        }
    }
}
