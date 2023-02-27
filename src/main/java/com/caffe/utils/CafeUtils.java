package com.caffe.utils;

import com.caffe.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    private CafeUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(Integer statusCode, boolean success, String responseMessage, HttpStatus httpStatus) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setCode(statusCode);
//        responseDTO.setSuccess(success);
//        responseDTO.setMessage(responseMessage);
//        responseDTO.setContent(null);
//        return new ResponseEntity(responseDTO, httpStatus);
        return new ResponseEntity<String>("{\"code\":\"" + statusCode + "\",\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
