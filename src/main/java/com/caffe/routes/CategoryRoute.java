package com.caffe.routes;

// ########################## CATEGORY ROUTES ##########################

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRoute {

    // ADD CATEGORY
    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);

    // GET ALL CATEGORY AT LEAST HAVE 1 PRODUCT
    @GetMapping(path = "get")
    ResponseEntity<String> getAllCategory(@RequestParam(required = false) boolean filterValue);

    // UPDATE CATEGORY
    @PutMapping(path = "/update/{id}")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap, @PathVariable Integer id);

    // DELETE CATEGORY
    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Integer id);
}
