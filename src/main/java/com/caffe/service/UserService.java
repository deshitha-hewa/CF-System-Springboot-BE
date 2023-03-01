package com.caffe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;


// ########################## USER BUSINESS LOGICS METHODS ##########################

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String> requestMap);

    ResponseEntity<String> login(Map<String,String> requestMap);
    ResponseEntity<String> getAllUsers();
}
