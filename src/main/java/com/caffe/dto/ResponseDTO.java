package com.caffe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

// ########################## RESPONSE MODEL ##########################

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDTO {
    private int status;
    private String message;
    private boolean success;
    private Object data;
}
