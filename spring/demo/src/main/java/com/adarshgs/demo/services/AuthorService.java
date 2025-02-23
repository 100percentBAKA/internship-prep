package com.adarshgs.demo.services;

import com.adarshgs.demo.dtos.AuthorDTO;
import com.adarshgs.demo.dtos.AuthorWithBookDTO;
import com.adarshgs.demo.dtos.AuthorWithBookPublisherDTO;
import com.adarshgs.demo.entities.Author;
import com.adarshgs.demo.entities.Book;
import com.adarshgs.demo.entities.Publisher;
import com.adarshgs.demo.mappers.AuthorMapper;
import com.adarshgs.demo.repositories.AuthorRepo;
import com.adarshgs.demo.repositories.BookRepo;
import com.adarshgs.demo.repositories.PublisherRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final AuthorMapper authorMapper;
    private final BookRepo bookRepo;
    private final PublisherRepo publisherRepo;

    public AuthorService(AuthorRepo authorRepo, AuthorMapper authorMapper, BookRepo bookRepo, PublisherRepo publisherRepo) {
        this.authorRepo = authorRepo;
        this.authorMapper = authorMapper;
        this.bookRepo = bookRepo;
        this.publisherRepo = publisherRepo;
    }

    @Transactional
    public void addAuthor(AuthorDTO authorDTO) {
        Book book = bookRepo.findById(authorDTO.getBookId()).orElseThrow(() -> new RuntimeException("Book with provided ID not found"));
        Author author = new Author();

        author.setName(authorDTO.getName());
        author.setBook(List.of(book));
        authorRepo.save(author);
    }

    @Transactional
    public void addAuthor(AuthorWithBookDTO authorWithBookDTO) {
        Publisher publisher = publisherRepo.findById(
                authorWithBookDTO.getBook().getPublisherId()
        ).orElseThrow(() -> new RuntimeException("Publisher not found"));

        Book book = new Book();
        book.setPublisher(publisher);
        book.setName(authorWithBookDTO.getBook().getName());

        Author author = new Author();
        author.setName(authorWithBookDTO.getName());
        author.setBook(List.of(book));

        authorRepo.save(author);
    }

    @Transactional
    public void addAuthor(AuthorWithBookPublisherDTO authorWithBookPublisherDTO) {
        Author author = new Author();

        Publisher publisher = new Publisher();
        publisher.setName(authorWithBookPublisherDTO.getBook().getPublisher().getName());

        Book book = new Book();
        book.setPublisher(publisher);
        book.setName(authorWithBookPublisherDTO.getBook().getName());

        author.setBook(List.of(book));
        author.setName(authorWithBookPublisherDTO.getName());

        authorRepo.save(author);
    }

    public List<Author> getAllAuthor() {
        return authorRepo.findAll();
    }
}
