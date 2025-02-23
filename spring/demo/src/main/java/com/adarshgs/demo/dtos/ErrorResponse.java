package com.adarshgs.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse extends NormalResponse {
    private String statusCode;

    public ErrorResponse(String statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
