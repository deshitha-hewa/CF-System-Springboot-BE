package com.caffe.serviceImpl;

import com.caffe.constants.CafeConstants;
import com.caffe.dto.UserDTO;
import com.caffe.entity.User;
import com.caffe.repository.UserRepository;
import com.caffe.service.UserService;
import com.caffe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j // for login
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Onside signup {}", requestMap);

        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepo.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepo.save(modelMapper.map(requestMap, User.class));
                    return CafeUtils.getResponseEntity(200, true, "Successfully Registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(401, false, "Email already exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(400, false, CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }
}
