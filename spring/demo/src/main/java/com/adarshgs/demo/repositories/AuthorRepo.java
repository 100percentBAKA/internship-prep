package com.adarshgs.demo.repositories;

import com.adarshgs.demo.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepo extends JpaRepository<Author, UUID> {
}
