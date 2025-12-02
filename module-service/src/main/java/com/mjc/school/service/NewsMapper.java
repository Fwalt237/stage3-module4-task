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

import java.util.List;

@Mapper(componentModel="spring")
public interface NewsMapper {

    @Mapping(target="authorDto", source="author")
    @Mapping(target="tagsDto", source="tags")
    @Mapping(target="commentsDto", source="comments")
    NewsDtoResponse toDto(News news);

    List<NewsDtoResponse> toDtoList(List<News> news);

    @Mapping(target="createdDate", ignore=true)
    @Mapping(target="lastUpdatedDate", ignore=true)
    @Mapping(target="comments", ignore=true)
    @Mapping(target="author",source="authorId", qualifiedByName="loadAuthor")
    @Mapping(target="tags", source="tagIds",qualifiedByName="loadTags" )
    News dtoToModel(NewsDtoRequest dto);

    @Named("loadAuthor")
    default Author loadAuthor(Long authorId, @Context AuthorRepository authorRepository){
        return authorRepository.getReferenceById(authorId);
    }

    @Named("loadTags")
    default List<Tag> loadTags(List<Long> tagIds, @Context TagRepository tagRepository){
        return tagIds == null ? List.of() : tagRepository.findAllById(tagIds);
    }
}

