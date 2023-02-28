package com.caffe.JWT;

import com.caffe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

// ########################## CHECK USER IN DB AND SEND DATA FOR AUTHENTICATION ##########################

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private com.caffe.entity.User userDetail;

    // Load user
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}",username);
        userDetail =userRepository.findByEmailId(username);
        if(!Objects.isNull(userDetail)) {
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }
        else
            throw  new UsernameNotFoundException("User not found");
    }

    public  com.caffe.entity.User getUserDetail(){
        return userDetail;
    }
}
