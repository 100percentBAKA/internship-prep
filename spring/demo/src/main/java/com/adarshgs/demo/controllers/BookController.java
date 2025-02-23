package com.adarshgs.demo.controllers;

import com.adarshgs.demo.dtos.BookDTO;
import com.adarshgs.demo.dtos.BookWithPublisherDTO;
import com.adarshgs.demo.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.adarshgs.demo.dtos.NormalResponse;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
    
    @PostMapping
    public ResponseEntity<NormalResponse> addBook(@Valid @RequestBody BookDTO bookDTO) {
        bookService.addBook(bookDTO);
        return ResponseEntity.ok().body(new NormalResponse("Book added successfully"));
    }

    @PostMapping("/withPublisher")
    public ResponseEntity<NormalResponse> addBook(@Valid @RequestBody BookWithPublisherDTO bookWithPublisherDTO) {
        bookService.addBook(bookWithPublisherDTO);
        return ResponseEntity.ok().body(new NormalResponse("Book added successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok().body(bookService.getAll());
    }
}
