package com.technoidentity.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoDataFoundException extends RuntimeException {

    private String message;
}

