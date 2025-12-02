package com.mjc.school.service.impl;

import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.specification.TagSpecification;
import com.mjc.school.service.TagMapper;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
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
public class TagServiceImpl implements TagService {

    private final TagMapper mapper;
    private final TagRepository tagRepository;

    @Override
    public Page<TagDtoResponse> getAll(String searchName, Pageable pageable) {
        var specification =  TagSpecification.withSearch(searchName);
        return tagRepository.findAll(specification, pageable).map(mapper::toDto);
    }

    @Override
    public List<TagDtoResponse> getByNewsId(Long newsId) {
        return mapper.toDtoList(tagRepository.findAllByNewsId(newsId));
    }

    @Override
    public TagDtoResponse getById(Long id) {
        return tagRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tag not found for id: " + id));
    }

    @Override
    @Transactional
    public TagDtoResponse create(TagDtoRequest createRequest) {
        if(tagRepository.existsByNameIgnoreCase(createRequest.name())){
            throw new IllegalArgumentException("Tag " + createRequest.name() + " already exists");
        }
        Tag tag = tagRepository.save(mapper.dtoToModel(createRequest));
        return mapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDtoResponse update(Long id, TagDtoRequest updateRequest) {
        if(!id.equals(updateRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Tag tag = findByIdOrThrow(id);
        tag.setName(updateRequest.name());
        return mapper.toDto(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public TagDtoResponse patch(Long id, TagDtoRequest patchRequest) {
        if(!id.equals(patchRequest.id())){
            throw new IllegalArgumentException("Id in path and request body must match");
        }
        Tag tag = findByIdOrThrow(id);
        if(hasText(patchRequest.name())){
            tag.setName(patchRequest.name());
        }
        return mapper.toDto(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!tagRepository.existsById(id)) throw new ResourceNotFoundException("Tag with id "+id+" not found");
        tagRepository.deleteById(id);
    }

    private Tag findByIdOrThrow(Long id){
        return tagRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Tag with id "+id+" not found"));
    }
}
