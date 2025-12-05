package com.mjc.school.controller.impl;

import com.mjc.school.controller.assembler.TagModelAssembler;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/stage3-module4-task/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagModelAssembler assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TagDtoResponse>>> getAll(
            @RequestParam(required=false) String searchName,
            @PageableDefault(size=20,sort="id,desc") Pageable pageable){

        Page<TagDtoResponse> page = tagService.getAll(searchName,pageable);

        return ResponseEntity.ok(assembler.toPagedModel(page,searchName,pageable));
    }

    @GetMapping("/by-news/{newsId}")
    public ResponseEntity<CollectionModel<EntityModel<TagDtoResponse>>> getByNewsId(@PathVariable Long newsId){
        List<TagDtoResponse> tags = tagService.getByNewsId(newsId);
        var models = tags.stream().map(dto->assembler.toModel(dto,dto.id())).toList();
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TagDtoResponse>> getById(@PathVariable Long id) {
        TagDtoResponse tag = tagService.getById(id);
        return ResponseEntity.ok(assembler.toModel(tag,id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<TagDtoResponse>> create(
            @RequestBody @Validated(Mandatory.class) TagDtoRequest createRequest) {
        TagDtoResponse tag = tagService.create(createRequest);
        var model = assembler.toModel(tag,tag.id());

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TagDtoResponse>> update(
            @PathVariable Long id, @RequestBody @Validated(Mandatory.class) TagDtoRequest updateRequest) {
        TagDtoResponse tag = tagService.update(id,updateRequest);
        return ResponseEntity.ok(assembler.toModel(tag,id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<TagDtoResponse>> patch(
            @PathVariable Long id, @RequestBody @Valid TagDtoRequest patchRequest) {
        TagDtoResponse tag = tagService.patch(id,patchRequest);
        return ResponseEntity.ok(assembler.toModel(tag,id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
