package com.adarshgs.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adarshgs.demo.entities.Publisher;

@Repository
public interface PublisherRepo extends JpaRepository<Publisher, UUID> {

}
