package com.caffe.serviceImpl;

import com.caffe.JWT.JwtFilter;
import com.caffe.constants.CafeConstants;
import com.caffe.entity.Category;
import com.caffe.repository.CategoryRepository;
import com.caffe.service.CategoryService;
import com.caffe.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
            if (jwtFilter.isAdmin()) {
                // Validate category
                if (validateCategoryMap(requestMap, false)) {
                    Category category = categoryRepo.save(modelMapper.map(requestMap, Category.class));
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.CATEGORY_CREATE_SUCCESS, category, HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(400, false, CafeConstants.INVALID_DATA, new ArrayList<>(), HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // VALIDATE CATEGORY | This method used for * addNewCategory & updateCategory *
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    // GET ALL CATEGORY
    @Override
    public ResponseEntity<String> getAllCategory(boolean filterValue) {
        try {
            if (filterValue) {
                log.info("Inside if");
                List<Category> categoryList= categoryRepo.getAllCategory();
                return CafeUtils.getResponseEntity(200, true, CafeConstants.DATA_FOUND, categoryList, HttpStatus.OK);
            }
            List<Category> categoryList2= categoryRepo.getAllCategory();
            return CafeUtils.getResponseEntity(200, true, CafeConstants.DATA_FOUND, categoryList2, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // CATEGORY UPDATE
    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap,Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                requestMap.put("id", id.toString());
                if(validateCategoryMap(requestMap,true)){
                    Optional optional=categoryRepo.findById(id);

                    if(!optional.isEmpty()){
                       Category category= categoryRepo.save(modelMapper.map(requestMap,Category.class));
                        return CafeUtils.getResponseEntity(200, true, CafeConstants.CATEGORY_UPDATE_SUCCESS, category, HttpStatus.OK);
                    }else {
                        return CafeUtils.getResponseEntity(404, false, CafeConstants.CATEGORY_ID_NOT_EXIST, new ArrayList<>(), HttpStatus.NOT_FOUND);
                    }
                }
                return CafeUtils.getResponseEntity(400, false, CafeConstants.DATA_NOT_FOUND, new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // DELETE CATEGORY
    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                Optional optional=categoryRepo.findById(id);
                if(!optional.isEmpty()){
                    categoryRepo.deleteById(id);
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.CATEGORY_DELETE_SUCCESS, new ArrayList<>(), HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity(404, false, CafeConstants.CATEGORY_ID_NOT_EXIST, new ArrayList<>(), HttpStatus.NOT_FOUND);
                }
            }else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
