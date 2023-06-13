package com.technoidentity.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionResponse {
    private int status;
    private Map<String, String> errors;
}


