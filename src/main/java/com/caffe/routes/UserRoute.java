package com.caffe.routes;

import com.caffe.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// ########################## USER ROUTES ##########################

@RequestMapping(path = "/user")
public interface UserRoute {

    // SIGNUP ROUTER
    @PostMapping(path = "/signup")
    public ResponseEntity<String> signup(@RequestBody(required = true) Map<String, String> requestMap);

    // LOGIN ROUTER
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    // GET ALL USERS FOR ADMIN
    @GetMapping(path = "/get")
    public ResponseEntity getAllUsers();

    // UPDATE USER STATUS FOR ADMIN
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateUserStatus(@RequestBody(required = true) Map<String, Boolean> requestMap, @PathVariable Long id);

    // CHECK TOKEN
    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    // CHANGE PASSWORD
    @PutMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);
}
