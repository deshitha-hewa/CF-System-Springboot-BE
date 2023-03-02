package com.caffe.serviceImpl;

import com.caffe.JWT.JwtFilter;
import com.caffe.constants.CafeConstants;
import com.caffe.entity.Category;
import com.caffe.repository.CategoryRepository;
import com.caffe.service.CategoryService;
import com.caffe.utils.CafeUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private ModelMapper modelMapper;

    // ADD CATEGORY
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                // Validate category
                if(validateCategoryMap(requestMap, false)){
                    Category category=categoryRepo.save(modelMapper.map(requestMap,Category.class));
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.CATEGORY_CREATE_SUCCESS, category, HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // VALIDATE CATEGORY | This method used for * addNewCategory & updateCategory *
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
}
