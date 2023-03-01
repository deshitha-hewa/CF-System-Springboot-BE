package com.caffe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;


// ########################## USER BUSINESS LOGICS METHODS ##########################

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<String> getAllUsers();

    ResponseEntity<String> updateUserStatus(Map<String, Boolean> requestMap, Long id);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> fogotPassword(Map<String, String> requestMap);
}
