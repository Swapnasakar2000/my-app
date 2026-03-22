package com.example.user_service.controller;

import com.example.user_service.config.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private JwtUtil jwtUtil;

//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        // Normally, validate username/password from DB
//        if ("admin".equals(username) && "password".equals(password)) {
//           // logger.info(jwtUtil.generateToken(username));
//            return jwtUtil.generateToken(username);
//        } else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
@PostMapping("/login")
public ResponseEntity<?> login(HttpServletResponse response) {

    String token = jwtUtil.generateToken("hardcodedUser"); // your logic

    Cookie cookie = new Cookie("swapna", token);
    cookie.setHttpOnly(true);     // prevents JS access
    cookie.setSecure(false);      // true in production (HTTPS)
    cookie.setPath("/");          // available for all APIs
    cookie.setMaxAge(24 * 60 * 60); // 1 day

    //without this line user has to take the token manually and put it inside session
    response.addCookie(cookie);

    return ResponseEntity.status(HttpStatus.OK).body(token);
}
}
