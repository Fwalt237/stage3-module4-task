package com.mjc.school.service.impl;
import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.specification.AuthorSpecification;
import com.mjc.school.service.AuthorMapper;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthorServiceImpl implements AuthorService{

    private final AuthorMapper mapper;
    private final AuthorRepository authorRepository;

    @Override
    public Page<AuthorDtoResponse> getAll(String searchName, Pageable pageable) {
        var specification = AuthorSpecification.withSearch(searchName);
        return authorRepository.findAll(specification,pageable).map(mapper::toDto);
    }

    @Override
    public AuthorDtoResponse getByNewsId(Long newsId) {
        return authorRepository.findByNewsId(newsId)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Author not found for news with id: " + newsId));
    }

    @Override
    public AuthorDtoResponse getById(Long id) {
        return authorRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Author not found for id: " + id));
    }

    @Override
    @Transactional
    public AuthorDtoResponse create(AuthorDtoRequest createRequest) {
        if(authorRepository.existsByNameIgnoreCase(createRequest.name())){
            throw new IllegalArgumentException("Author " + createRequest.name() + " already exists");
        }
        Author author = authorRepository.save(mapper.dtoToModel(createRequest));
        return mapper.toDto(author);
    }

    @Override
    @Transactional
    public AuthorDtoResponse update(Long id, AuthorDtoRequest updateRequest) {
        if(!id.equals(updateRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Author author = findByIdOrThrow(id);
        author.setName(updateRequest.name());
        return mapper.toDto(authorRepository.save(author));
    }

    @Override
    @Transactional
    public AuthorDtoResponse patch(Long id, AuthorDtoRequest patchRequest) {
        if(!id.equals(patchRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Author author = findByIdOrThrow(id);
        if(hasText(patchRequest.name())){
            author.setName(patchRequest.name());
        }
        return mapper.toDto(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!authorRepository.existsById(id)) throw new ResourceNotFoundException("Author with id "+id+" not found");
        authorRepository.deleteById(id);
    }

    private Author findByIdOrThrow(Long id){
        return authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author with id "+id+" not found"));
    }
}
