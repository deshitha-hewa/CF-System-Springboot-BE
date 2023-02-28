package com.caffe.utils;

import com.caffe.dto.JwtResponseDTO;
import com.caffe.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// ########################## UNIQUE RESPONSE ##########################

public class CafeUtils {
    private CafeUtils() {

    }

    // Response
    public static ResponseEntity<String> getResponseEntity(Integer statusCode, boolean success, String responseMessage,Object data, HttpStatus httpStatus) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(statusCode);
        responseDTO.setSuccess(success);
        responseDTO.setMessage(responseMessage);
        responseDTO.setData(data);
        return new ResponseEntity(responseDTO, httpStatus);
    }

    // Token response
    public static ResponseEntity<String> getTokenResponse(String token, HttpStatus httpStatus) {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(token);
        return new ResponseEntity(jwtResponseDTO, httpStatus);
    }
}
