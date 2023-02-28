package com.caffe.serviceImpl;

import com.caffe.JWT.CustomerUserDetailsService;
import com.caffe.JWT.JwtUtil;
import com.caffe.constants.CafeConstants;
import com.caffe.entity.User;
import com.caffe.repository.UserRepository;
import com.caffe.service.UserService;
import com.caffe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

// ########################## USER BUSINESS LOGICS IMPLEMENTATION ##########################

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    // SIGNUP METHOD IMPLEMENTATION
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);

        try {
            if (validateSignUpMap(requestMap)) {
                // Check user is already in the db | Call the UserRepository
                User user = userRepo.findByEmailId(requestMap.get("email"));

                if (Objects.isNull(user)) {
                    // Map body with model
                    User user1 =userRepo.save(modelMapper.map(requestMap, User.class));
//                    user1.setPassword("");
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.REG_SUCCESS,user1, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(401, false, CafeConstants.EMAIL_EXIST,null, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(400, false, CafeConstants.INVALID_DATA,null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG,null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // VALIDATE SIGNUP INPUTS
    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    // LOGIN METHOD IMPLEMENTATION
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Onside login");
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                // Check user is approved or not
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return CafeUtils.getTokenResponse(jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(), customerUserDetailsService.getUserDetail().getRole()), HttpStatus.BAD_REQUEST);
//                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(), customerUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity(400, false, CafeConstants.WAIT_ADMIN_APPROVAL,null, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG,null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
