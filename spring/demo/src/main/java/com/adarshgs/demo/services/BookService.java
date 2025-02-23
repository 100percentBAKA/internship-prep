package com.adarshgs.demo.services;

import com.adarshgs.demo.dtos.BookDTO;
import com.adarshgs.demo.dtos.BookWithPublisherDTO;
import com.adarshgs.demo.entities.Book;
import com.adarshgs.demo.entities.Publisher;
import com.adarshgs.demo.exceptions.PublisherNotFound;
import com.adarshgs.demo.mappers.BookMapper;
import com.adarshgs.demo.mappers.PublisherMapper;
import com.adarshgs.demo.repositories.BookRepo;
import com.adarshgs.demo.repositories.PublisherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;
    private final BookMapper bookMapper;
    private final PublisherRepo publisherRepo;
    private final PublisherMapper publisherMapper;

    @Autowired
    public BookService(BookRepo bookRepo, BookMapper bookMapper, PublisherRepo publisherRepo, PublisherMapper publisherMapper) {
        this.bookRepo = bookRepo;
        this.bookMapper = bookMapper;
        this.publisherRepo = publisherRepo;
        this.publisherMapper = publisherMapper;
    }

    public void addBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO); // publisherId will be null here
        Publisher pub = publisherRepo.findById(bookDTO.getPublisherId()).orElseThrow(() -> new PublisherNotFound("Publisher with the ID not found"));

        book.setPublisher(pub);
        bookRepo.save(book);
    }

    public void addBook(BookWithPublisherDTO bookWithPublisherDTO) {
        Publisher publisher = publisherMapper.toEntity(bookWithPublisherDTO.getPublisher());
        Book book = new Book();
        book.setName(bookWithPublisherDTO.getName());
        book.setPublisher(publisher);

        bookRepo.save(book);
    }

    public List<Book> getAll() {
        return bookRepo.findAll();
    }
}
