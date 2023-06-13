package com.technoidentity.exception;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonApiResponse {

    private String message;
    private StackTraceElement[] trace;
    private int httpStatus;
    private long timeStamp;


}
