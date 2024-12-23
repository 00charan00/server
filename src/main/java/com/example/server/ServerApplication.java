package com.example.server;

import com.example.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    @Autowired
    StaffService staffService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        staffService.createAdminAccount();
    }
}
