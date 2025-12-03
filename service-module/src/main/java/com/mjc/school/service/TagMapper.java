package com.mjc.school.service;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface TagMapper {

    TagDtoResponse toDto(Tag tag);

    List<TagDtoResponse> toDtoList(List<Tag> tags);

    @Mapping(target="news", ignore=true)
    Tag dtoToModel(TagDtoRequest dto);
}
