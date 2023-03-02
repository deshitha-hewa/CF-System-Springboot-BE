package com.caffe.controller;

import com.caffe.constants.CafeConstants;
import com.caffe.routes.CategoryRoute;
import com.caffe.service.CategoryService;
import com.caffe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

// ########################## CATEGORY CONTROLLER ##########################

@RestController
public class CategoryController implements CategoryRoute {

    @Autowired
    CategoryService categoryService;

    // CRETE CATEGORY
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
