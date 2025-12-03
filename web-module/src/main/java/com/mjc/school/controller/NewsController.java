package com.mjc.school.controller;

import com.mjc.school.controller.assembler.NewsModelAssembler;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.validated.Mandatory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stage3-module4-task/v1/news")
@RequiredArgsConstructor
@Validated
public class NewsController{

    private final NewsService newsService;
    private final NewsModelAssembler assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<NewsDtoResponse>>> getAll(
            @RequestParam(required=false)List<String> tagNames,
            @RequestParam(required=false)List<Long> tagIds,
            @RequestParam(required=false)String authorName,
            @RequestParam(required=false)String title,
            @RequestParam(required=false)String content,
            @PageableDefault(size=20,sort="createdDate,desc") Pageable pageable){

        Page<NewsDtoResponse> page = newsService.getAll(tagNames, tagIds, authorName, title, content,pageable);

        return ResponseEntity.ok(assembler.toPagedModel(page,tagNames, tagIds, authorName, title, content,pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<NewsDtoResponse>>> getByParams(
            @RequestParam(required=false)List<String> tagNames,
            @RequestParam(required=false)List<Long> tagIds,
            @RequestParam(required=false)String authorName,
            @RequestParam(required=false)String title,
            @RequestParam(required=false)String content,
            @RequestParam(defaultValue="createdDate,desc") String sort){

        Sort sortObject = Sort.by(Sort.Direction.fromString(sort.endsWith(",desc")?"DESC":"ASC"),sort.split(":")[0]);
        List<NewsDtoResponse> news = newsService.getByParams(tagNames, tagIds, authorName, title, content,sortObject);
        var models = news.stream().map(dto->assembler.toModel(dto,dto.id())).toList();

        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<NewsDtoResponse>> getById(@PathVariable Long id) {
        NewsDtoResponse news = newsService.getById(id);
        return ResponseEntity.ok(assembler.toModel(news,id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<NewsDtoResponse>> create(
            @RequestBody @Validated(Mandatory.class) NewsDtoRequest createRequest) {
        NewsDtoResponse news = newsService.create(createRequest);
        var model = assembler.toModel(news,news.id());

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<NewsDtoResponse>> update(
            @PathVariable Long id, @RequestBody @Validated(Mandatory.class) NewsDtoRequest updateRequest) {
        NewsDtoResponse news = newsService.update(id,updateRequest);
        return ResponseEntity.ok(assembler.toModel(news,id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<NewsDtoResponse>> patch(
            @PathVariable Long id, @RequestBody @Valid NewsDtoRequest patchRequest) {
        NewsDtoResponse news = newsService.patch(id,patchRequest);
        return ResponseEntity.ok(assembler.toModel(news,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
