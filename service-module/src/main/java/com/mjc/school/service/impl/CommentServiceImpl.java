package com.mjc.school.service.impl;

import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.specification.CommentSpecification;
import com.mjc.school.service.CommentMapper;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CommentServiceImpl implements CommentService {

    private final CommentMapper mapper;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    @Override
    public Page<CommentDtoResponse> getAll(String searchContent, Pageable pageable) {
        var specification = CommentSpecification.withSearch(searchContent);
        return commentRepository.findAll(specification,pageable).map(mapper::toDto);
    }

    @Override
    public List<CommentDtoResponse> getByNewsId(Long newsId) {
        return mapper.toDtoList(commentRepository.findAllByNewsId(newsId));
    }

    @Override
    public CommentDtoResponse getById(Long id) {
        return commentRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comment not found for id: " + id));
    }

    @Override
    @Transactional
    public CommentDtoResponse create(CommentDtoRequest createRequest) {
        News news = newsRepository.findById(createRequest.newsId())
                .orElseThrow(()-> new ResourceNotFoundException("News with id "+createRequest.newsId()+" not found"));
        Comment comment = new Comment();
        comment.setNews(news);
        comment.setContent(createRequest.content());
        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDtoResponse update(Long id, CommentDtoRequest updateRequest) {
        if(!id.equals(updateRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Comment comment = findByIdOrThrow(id);
        comment.setContent(updateRequest.content());
        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDtoResponse patch(Long id, CommentDtoRequest patchRequest) {
        if(!id.equals(patchRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Comment comment = findByIdOrThrow(id);
        if(hasText(patchRequest.content())){
            comment.setContent(patchRequest.content());
        }
        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) throw new ResourceNotFoundException("Comment with id "+id+" not found");
        commentRepository.deleteById(id);
    }

    private Comment findByIdOrThrow(Long id){
        return commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment with id "+id+" not found"));
    }
}
