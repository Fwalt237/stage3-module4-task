package com.mjc.school.service;

import com.mjc.school.repository.model.Author;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface AuthorMapper {

    AuthorDtoResponse toDto(Author author);

    List<AuthorDtoResponse> toDtoList(List<Author> authors);

    @Mapping(target="createdDate", ignore=true)
    @Mapping(target="lastUpdatedDate", ignore=true)
    @Mapping(target="news", ignore=true)
    Author dtoToModel(AuthorDtoRequest dto);
}
