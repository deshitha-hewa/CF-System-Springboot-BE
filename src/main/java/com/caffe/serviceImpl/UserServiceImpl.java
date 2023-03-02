package com.caffe.serviceImpl;

import com.caffe.JWT.CustomerUserDetailsService;
import com.caffe.JWT.JwtFilter;
import com.caffe.JWT.JwtUtil;
import com.caffe.constants.CafeConstants;
import com.caffe.entity.User;
import com.caffe.repository.UserRepository;
import com.caffe.service.UserService;
import com.caffe.utils.CafeUtils;
import com.caffe.utils.EmailUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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
    JwtFilter jwtFilter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    EmailUtils emailUtils;

    // SIGNUP METHOD IMPLEMENTATION
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);

        try {
            if (validateSignUpMap(requestMap)) {
                // Check user is already in the db | Call the UserRepository
                User user = userRepo.findByEmailId(requestMap.get("email"));

                if (Objects.isNull(user)) {
                    // log.info("Onside login {}", user);
                    // Map body with model
                    User user1 = userRepo.save(modelMapper.map(requestMap, User.class));
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.USER_REG_SUCCESS, user1, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(401, false, CafeConstants.EMAIL_EXIST, new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(400, false, CafeConstants.INVALID_DATA, new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
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
                if (customerUserDetailsService.getUserDetail().getStatus().equals(true)) {
                    return CafeUtils.getTokenResponse(jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(), customerUserDetailsService.getUserDetail().getRole()), HttpStatus.BAD_REQUEST);
                } else {
                    return CafeUtils.getResponseEntity(400, false, CafeConstants.WAIT_ADMIN_APPROVAL, new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // GET ALL USERS METHOD IMPLEMENTATION
    @Override
    public ResponseEntity<String> getAllUsers() {
        try {
            // Check admin token
            if (jwtFilter.isAdmin()) {
                List<User> user = userRepo.getAllUser();
                return CafeUtils.getResponseEntity(200, true, CafeConstants.DATA_FOUND, user, HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // UPDATE USER STATUS FOR ADMIN
    @Override
    public ResponseEntity<String> updateUserStatus(Map<String, Boolean> requestMap, Long id) {
        try {
            // Check admin token
            if (jwtFilter.isAdmin()) {
                // Check user in db
                Optional<User> optionalUser = userRepo.findById(Integer.parseInt(id.toString()));

                if (!optionalUser.isEmpty()) {

                    userRepo.updateStatus(requestMap.get("status"), id);

                    optionalUser = userRepo.findById(Integer.parseInt(id.toString()));

                    // Send email
                    sendEmailToAllAdmin(requestMap.get("status"), optionalUser.get().getEmail(), userRepo.getAllAdmin());

                    return CafeUtils.getResponseEntity(200, true, CafeConstants.USER_STATUS_UPDATE_SUCCESS, optionalUser, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(404, false, CafeConstants.USERID_NOT_EXIST, new ArrayList<>(), HttpStatus.NOT_FOUND);
                }
            } else {
                return CafeUtils.getResponseEntity(401, false, CafeConstants.UNAUTHORIZED_ACCESS, new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // Send mail to all admin
    private void sendEmailToAllAdmin(Boolean status, String userEmail, List<String> allAdmin) {
        // Remove current user
        allAdmin.remove(jwtFilter.getCurrentUser());

        if (status != null && status.equals(true)) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved", "USER:- " + userEmail + "\n is approved by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account disabled", "USER:- " + userEmail + "\n is disabled by \nADMIN: " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    // CHECK TOKEN
    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity(200, true, CafeConstants.USER_STATUS_UPDATE_SUCCESS, new ArrayList<>(), HttpStatus.OK);
    }

    // UPDATE PASSWORD
    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmail(jwtFilter.getCurrentUser());
            if(!user.equals(null)){
                // Check old password
                if(user.getPassword().equals(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepo.save(user);
                    return CafeUtils.getResponseEntity(200, true, CafeConstants.PASSWORD_UPDATE_SUCCESS, new ArrayList<>(), HttpStatus.OK);

                }
                return CafeUtils.getResponseEntity(400, false, CafeConstants.OLD_PASSWORD_INCORRECT, new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // FORGOT PASSWORD
    @Override
    public ResponseEntity<String> fogotPassword(Map<String, String> requestMap) {
        try {
            User user=userRepo.findByEmail(requestMap.get("email"));

            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+user);

                emailUtils.forgotMail(user.getEmail(),"Credentials by Cafe Management System" ,user.getPassword());

            return CafeUtils.getResponseEntity(200, true, CafeConstants.CHECK_EMAIL_CREDENTIALS, new ArrayList<>(), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(500, false, CafeConstants.SOMETHING_WENT_WRONG, new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
