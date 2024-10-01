package com.designer.graphic.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        // Simple authentication logic
//        if ("jetly".equals(username) && "jetly@123".equals(password)) {
//            return "Login successful";
//        }
//        return "Invalid credentials";
//    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();

        if ("jetly".equals(username) && "jetly@123".equals(password)) {
            // Example JWT token (in real application, generate this token)
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM3MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

            // User details
            Map<String, Object> user = new HashMap<>();
            user.put("id", 1);
            user.put("username", "jetly");
            user.put("email", "jetlyjohn1212@gmail.com");

            // Building response with status and message
            response.put("status", "success");
            response.put("message", "Login successfully");
            response.put("token", token);
            response.put("user", user);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid credentials");
        }

        return response;
    }
}
