package com.adarshgs.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.adarshgs.demo.dtos.NormalResponse;
import com.adarshgs.demo.dtos.PublisherDTO;
import com.adarshgs.demo.services.PublisherService;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/publisher")
public class PublisherController {
    private final PublisherService pubService;

    @Autowired
    public PublisherController(PublisherService pubService) {
        this.pubService = pubService;
    }
    
    @PostMapping
    public ResponseEntity<NormalResponse> addPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        pubService.addPublisher(publisherDTO);
        return ResponseEntity.ok().body(new NormalResponse("Publisher added successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(pubService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(pubService.getPublisherId(id));
    }
}
