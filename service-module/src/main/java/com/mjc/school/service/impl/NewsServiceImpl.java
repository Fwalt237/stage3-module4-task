package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.specification.NewsSpecification;
import com.mjc.school.service.NewsMapper;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class NewsServiceImpl implements NewsService{

    private final NewsMapper mapper;
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    @Override
    public Page<NewsDtoResponse> getAll(List<String> tagNames,
                                        List<Long> tagIds,
                                        String authorName,
                                        String title,
                                        String content,
                                        Pageable pageable) {
        var specification =  NewsSpecification.withSearch(tagNames, tagIds, authorName, title, content);
        return newsRepository.findAll(specification, pageable).map(mapper::toDto);
    }

    @Override
    public List<NewsDtoResponse> getByParams(List<String> tagNames,
                                             List<Long> tagIds,
                                             String authorName,
                                             String title,
                                             String content, Sort sort) {
        var specification =  NewsSpecification.withSearch(tagNames, tagIds, authorName, title, content);
        return newsRepository.findAll(specification,sort).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public NewsDtoResponse getById(Long id) {
        return newsRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "News not found for id: " + id));
    }

    @Override
    @Transactional
    public NewsDtoResponse create(NewsDtoRequest createRequest) {
        doesAuthorExist(createRequest.authorId());
        doTagsExist(createRequest.tagIds());
        News news = newsRepository.save(mapper.dtoToModel(createRequest));
        return mapper.toDto(news);
    }

    @Override
    @Transactional
    public NewsDtoResponse update(Long id, NewsDtoRequest updateRequest) {
        if(!id.equals(updateRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        News news = findByIdOrThrow(id);
        doesAuthorExist(updateRequest.authorId());
        doTagsExist(updateRequest.tagIds());
        news.setTitle(updateRequest.title());
        news.setContent(updateRequest.content());
        news.setAuthor(authorRepository.getReferenceById(updateRequest.authorId()));

        setTags(updateRequest, news);
        return mapper.toDto(newsRepository.save(news));
    }

    @Override
    @Transactional
    public NewsDtoResponse patch(Long id, NewsDtoRequest patchRequest) {
        if(!id.equals(patchRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        News news = findByIdOrThrow(id);
        if(hasText(patchRequest.title())) news.setTitle(patchRequest.title());
        if(hasText(patchRequest.content())) news.setContent(patchRequest.content());
        if(patchRequest.authorId()!=null){
            doesAuthorExist(patchRequest.authorId());
            news.setAuthor(authorRepository.getReferenceById(patchRequest.authorId()));
        }
        setTags(patchRequest, news);
        return mapper.toDto(newsRepository.save(news));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!newsRepository.existsById(id)) throw new ResourceNotFoundException("News with id "+id+" not found");
        newsRepository.deleteById(id);
    }

    private void doesAuthorExist(Long id){
        if(!authorRepository.existsById(id)){
            throw new IllegalArgumentException("Author with id "+id+" doesn't exist");
        }
    }

    private void doTagsExist(List<Long> ids){
        if(ids !=null && !ids.isEmpty()){
            Long count = tagRepository.countByIdIn(ids);
            if(count != ids.size()){
                throw new IllegalArgumentException("One or more tag Ids are invalid");
            }
        }
    }

    private News findByIdOrThrow(Long id){
        return newsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("News with id "+id+" not found"));
    }

    private void setTags(NewsDtoRequest request, News news) {
        if(request.tagIds()!=null){
            doTagsExist(request.tagIds());
            List<Tag> tagReferences = request.tagIds().stream()
                    .map(tagRepository::getReferenceById)
                    .toList();
            news.setTags(tagReferences);
        }else{
            news.setTags(List.of());
        }
    }
}
