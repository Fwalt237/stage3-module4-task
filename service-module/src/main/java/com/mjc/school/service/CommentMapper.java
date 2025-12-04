package com.mjc.school.service;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Mapper(componentModel="spring", uses ={NewsMapper.class})
public abstract class CommentMapper {

    @Autowired
    protected NewsRepository newsRepository;

    public abstract CommentDtoResponse toDto(Comment comment);

    public abstract List<CommentDtoResponse> toDtoList(List<Comment> comments);

    @Mapping(target="created", ignore=true)
    @Mapping(target="modified", ignore=true)
    @Mapping(target = "news", expression =
            "java(newsRepository.getReferenceById(dto.newsId()))")
    public abstract Comment dtoToModel(CommentDtoRequest dto);

}
