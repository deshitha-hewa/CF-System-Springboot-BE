package com.caffe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<String> getAllCategory(boolean filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap,Integer id);
}
