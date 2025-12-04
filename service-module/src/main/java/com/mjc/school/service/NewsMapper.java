package com.mjc.school.service;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel="spring", uses = { AuthorMapper.class, TagMapper.class})
public abstract class NewsMapper {

    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected TagRepository tagRepository;

    @Mapping(target="authorDto", source="author")
    @Mapping(target="tagsDto", source="tags")
    @Mapping(target="commentsDto", source="comments")
    public abstract  NewsDtoResponse toDto(News news);

    public abstract  List<NewsDtoResponse> toDtoList(List<News> news);

    @Mapping(target="createdDate", ignore=true)
    @Mapping(target="lastUpdatedDate", ignore=true)
    @Mapping(target="comments", ignore=true)
    @Mapping(target = "author", expression =
            "java(authorRepository.getReferenceById(dto.authorId()))")
    @Mapping(target = "tags", expression =
            "java(dto.tagIds().stream().map(tagId -> tagRepository.getReferenceById(tagId)).toList())")
    public abstract  News dtoToModel(NewsDtoRequest dto);

}

