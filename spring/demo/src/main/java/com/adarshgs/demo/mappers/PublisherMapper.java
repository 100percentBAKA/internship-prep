package com.adarshgs.demo.mappers;

import org.mapstruct.Mapper;

import com.adarshgs.demo.dtos.PublisherDTO;
import com.adarshgs.demo.entities.Publisher;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    Publisher toEntity(PublisherDTO publisherDTO);
}
