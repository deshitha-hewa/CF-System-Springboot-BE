package com.caffe.routes;

// ########################## CATEGORY ROUTES ##########################

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRoute {

    // ADD CATEGORY
    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true)Map<String,String> requestMap);

    // GET ALL CATEGORY AT LEAST HAVE 1 PRODUCT
//    @GetMapping(path = "getAllCategory")
//    public ResponseEntity getAllCategory();

}
