package com.mjc.school.controller.impl;

import com.mjc.school.controller.assembler.CommentModelAssembler;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
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
@RequestMapping("/stage3-module4-task/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentModelAssembler assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CommentDtoResponse>>> getAll(
            @RequestParam(required=false) String searchContent,
            @PageableDefault(size=20,sort="created,desc") Pageable pageable){

        Page<CommentDtoResponse> page = commentService.getAll(searchContent,pageable);

        return ResponseEntity.ok(assembler.toPagedModel(page,searchContent,pageable));
    }

    @GetMapping("/by-news/{newsId}")
    public ResponseEntity<CollectionModel<EntityModel<CommentDtoResponse>>> getByNewsId(@PathVariable Long newsId){
        List<CommentDtoResponse> comments = commentService.getByNewsId(newsId);
        var models = comments.stream().map(dto->assembler.toModel(dto,dto.id())).toList();
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CommentDtoResponse>> getById(@PathVariable Long id) {
        CommentDtoResponse comment = commentService.getById(id);
        return ResponseEntity.ok(assembler.toModel(comment,id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<CommentDtoResponse>> create(
            @RequestBody @Validated(Mandatory.class) CommentDtoRequest createRequest) {
        CommentDtoResponse comment = commentService.create(createRequest);
        var model = assembler.toModel(comment,comment.id());

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CommentDtoResponse>> update(
            @PathVariable Long id, @RequestBody @Validated(Mandatory.class) CommentDtoRequest updateRequest) {
        CommentDtoResponse comment = commentService.update(id,updateRequest);
        return ResponseEntity.ok(assembler.toModel(comment,id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<CommentDtoResponse>> patch(
            @PathVariable Long id, @RequestBody @Valid CommentDtoRequest patchRequest) {
        CommentDtoResponse comment = commentService.patch(id,patchRequest);
        return ResponseEntity.ok(assembler.toModel(comment,id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
