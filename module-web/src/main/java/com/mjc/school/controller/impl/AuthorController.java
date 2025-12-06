package com.mjc.school.controller.impl;

import com.mjc.school.controller.assembler.AuthorModelAssembler;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/stage3-module4-task/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorModelAssembler assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AuthorDtoResponse>>> getAll(
            @RequestParam(required=false) String searchName,
            @PageableDefault(size=20,sort="createdDate,desc") Pageable pageable){

        Page<AuthorDtoResponse> page = authorService.getAll(searchName,pageable);

        return ResponseEntity.ok(assembler.toPagedModel(page,searchName,pageable));
    }

    @GetMapping("/by-news/{newsId}")
    public ResponseEntity<EntityModel<AuthorDtoResponse>> getByNewsId(@PathVariable Long newsId){
        AuthorDtoResponse author = authorService.getByNewsId(newsId);
        return ResponseEntity.ok(assembler.toModel(author,newsId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AuthorDtoResponse>> getById(@PathVariable Long id) {
        AuthorDtoResponse author = authorService.getById(id);
        return ResponseEntity.ok(assembler.toModel(author,id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<AuthorDtoResponse>> create(
            @RequestBody @Validated(Mandatory.class) AuthorDtoRequest createRequest) {
        AuthorDtoResponse author = authorService.create(createRequest);
        var model = assembler.toModel(author,author.id());

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AuthorDtoResponse>> update(
            @PathVariable Long id, @RequestBody @Validated(Mandatory.class) AuthorDtoRequest updateRequest) {
        AuthorDtoResponse author = authorService.update(id,updateRequest);
        return ResponseEntity.ok(assembler.toModel(author,id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<AuthorDtoResponse>> patch(
            @PathVariable Long id, @RequestBody @Valid AuthorDtoRequest patchRequest) {
        AuthorDtoResponse author = authorService.patch(id,patchRequest);
        return ResponseEntity.ok(assembler.toModel(author,id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
