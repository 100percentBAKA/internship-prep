package com.adarshgs.demo.controllers;

import com.adarshgs.demo.dtos.AuthorDTO;
import com.adarshgs.demo.dtos.AuthorWithBookDTO;
import com.adarshgs.demo.dtos.AuthorWithBookPublisherDTO;
import com.adarshgs.demo.dtos.NormalResponse;
import com.adarshgs.demo.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        authorService.addAuthor(authorDTO);

        return ResponseEntity.ok().body(new NormalResponse("Author successfully added into database"));
    }

    @PostMapping("/withBook")
    public ResponseEntity<?> addAuthor(@Valid @RequestBody AuthorWithBookDTO authorWithBookDTO) {
        authorService.addAuthor(authorWithBookDTO);
        return ResponseEntity.ok().body(new NormalResponse("Author added successfully"));
    }

    @PostMapping("/withBookPub")
    public ResponseEntity<?> addAuthor(@Valid @RequestBody AuthorWithBookPublisherDTO authorWithBookPublisherDTO) {
        authorService.addAuthor(authorWithBookPublisherDTO);
        return ResponseEntity.ok().body(new NormalResponse("Author added successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getAllAuthor() {
        return ResponseEntity.ok().body(authorService.getAllAuthor());
    }
}
