package com.adarshgs.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adarshgs.demo.dtos.NormalResponse;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<NormalResponse> sayHello() {
        return ResponseEntity.ok().body(new NormalResponse("Hello from Adarsh"));
    }
}
