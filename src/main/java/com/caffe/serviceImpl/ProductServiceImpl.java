package com.caffe.serviceImpl;

import com.caffe.JWT.JwtFilter;
import com.caffe.constants.CafeConstants;
import com.caffe.entity.Category;
import com.caffe.entity.Product;
import com.caffe.repository.ProductRepository;
import com.caffe.service.ProductService;
import com.caffe.utils.CafeUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepo;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ModelMapper modelMapper;

    // ADD PRODUCT
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap)) {
                    Category category = new Category();
                    category.setId(Integer.parseInt(requestMap.get("categoryId")));
                    requestMap.put("status", "true");
                    requestMap.put("category", category.getName());
                    Product product = productRepo.save(modelMapper.map(requestMap, Product.class));

                    return CafeUtils.getResponseEntity(200, true, CafeConstants.PRODUCT_CREATE_SUCCESS,  new ArrayList<>(), HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(400, false, CafeConstants.INVALID_DATA, new ArrayList<>(), HttpStatus.BAD_REQUEST);
            } else
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // VALIDATE PRODUCT | This method used for * addNewProduct & updateProduct *
    private boolean validateProductMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("categoryId") && requestMap.containsKey("description") && requestMap.containsKey("price")) {
            return true;
        }
        return false;
    }
}
