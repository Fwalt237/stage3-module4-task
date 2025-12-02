package com.mjc.school.service;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel="spring")
public interface CommentMapper {

    CommentDtoResponse toDto(Comment comment);

    List<CommentDtoResponse> toDtoList(List<Comment> comments);

    @Mapping(target="created", ignore=true)
    @Mapping(target="modified", ignore=true)
    @Mapping(target="news", source="newsId", qualifiedByName="loadNews")
    Comment dtoToModel(CommentDtoRequest dto);

    @Named("loadNews")
    default News loadNews(Long newsId, @Context NewsRepository newsRepository){
        return newsRepository.getReferenceById(newsId);
    }
}
