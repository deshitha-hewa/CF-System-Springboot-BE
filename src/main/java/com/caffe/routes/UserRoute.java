package com.caffe.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path="api/v1/user")
public interface UserRoute {

    @PostMapping(path="/signup")
    public ResponseEntity<String> signup(@RequestBody(required = true)Map<String,String> requestMap);
}
