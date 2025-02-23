package com.adarshgs.demo.mappers;

import com.adarshgs.demo.dtos.AuthorDTO;
import com.adarshgs.demo.entities.Author;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorDTO authorDTO);
}
