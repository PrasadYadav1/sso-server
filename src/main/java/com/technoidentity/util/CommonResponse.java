package com.technoidentity.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private UUID id;
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}
