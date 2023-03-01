package com.caffe.routes;

import com.caffe.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

// ########################## USER ROUTES ##########################

@RequestMapping(path="/user")
public interface UserRoute {

    // SIGNUP ROUTER
    @PostMapping(path="/signup")
    public ResponseEntity<String> signup(@RequestBody(required = true)Map<String,String> requestMap);

    // LOGIN ROUTER
    @PostMapping(path="/login")
    public ResponseEntity<String> login(@RequestBody(required = true)Map<String,String> requestMap);

    // GET ALL USERS FOR ADMIN
    @GetMapping(path = "/get")
    public ResponseEntity getAllUsers();
}
