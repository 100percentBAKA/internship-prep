package com.adarshgs.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarshgs.demo.dtos.PublisherDTO;
import com.adarshgs.demo.entities.Publisher;
import com.adarshgs.demo.mappers.PublisherMapper;
import com.adarshgs.demo.repositories.PublisherRepo;

@Service
public class PublisherService {
    private final PublisherRepo publisherRepo;
    private final PublisherMapper publisherMapper;

    @Autowired
    public PublisherService(PublisherRepo publisherRepo, PublisherMapper publisherMapper) {
        this.publisherRepo = publisherRepo;
        this.publisherMapper = publisherMapper;
    }

    public void addPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = publisherMapper.toEntity(publisherDTO);
        publisherRepo.save(publisher);
    }

    public List<Publisher> getAll() {
        return publisherRepo.findAll();
    }

    public Publisher getPublisherId(UUID id) {
         return publisherRepo.findById(id).orElseThrow(() -> new RuntimeException("Publisher with the provided ID not found"));
    }
}
