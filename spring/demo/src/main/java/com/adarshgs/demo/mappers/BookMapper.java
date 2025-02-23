package com.adarshgs.demo.mappers;

import org.mapstruct.Mapper;

import com.adarshgs.demo.dtos.BookDTO;
import com.adarshgs.demo.entities.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookDTO bookDTO);
}
