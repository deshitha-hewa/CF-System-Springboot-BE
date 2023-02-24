package com.caffe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    private CafeUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(Integer statusCode,String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"code\":\"" + statusCode + "\",\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
