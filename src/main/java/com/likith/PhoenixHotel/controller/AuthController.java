package com.likith.PhoenixHotel.controller;

import com.likith.PhoenixHotel.dto.LoginRequest;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.entity.User;
import com.likith.PhoenixHotel.service.interfac.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody User user){
        ResponseDTO response=userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequest loginRequest){
        ResponseDTO response=userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
