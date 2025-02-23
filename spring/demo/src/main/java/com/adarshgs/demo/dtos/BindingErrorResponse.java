package com.adarshgs.demo.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class BindingErrorResponse {
    private String statusCode;
    private Map<String, String> errors;
}
