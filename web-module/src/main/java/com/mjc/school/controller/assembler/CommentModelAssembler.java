package com.mjc.school.controller.assembler;

import com.mjc.school.controller.CommentController;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentModelAssembler {
    public EntityModel<CommentDtoResponse> toModel(CommentDtoResponse commentDtoResponse, Long id){

        var model = EntityModel.of(commentDtoResponse);

        model.add(linkTo(methodOn(CommentController.class).getById(id)).withSelfRel());
        model.add(linkTo(methodOn(CommentController.class).update(id,null)).withRel("update"));
        model.add(linkTo(methodOn(CommentController.class).patch(id,null)).withRel("patch"));
        model.add(linkTo(methodOn(CommentController.class).deleteById(id)).withRel("delete"));

        return model;
    }

    public PagedModel<EntityModel<CommentDtoResponse>> toPagedModel(
            Page<CommentDtoResponse> page,
            String searchContent,
            Pageable pageable){

        var entityModels = page.map(dto->toModel(dto,dto.id()));

        var metadata =  new PagedModel.PageMetadata(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        var pagedModel = PagedModel.of(entityModels.getContent(),metadata);

        pagedModel.add(linkTo(methodOn(CommentController.class)
                .getAll(searchContent,pageable)).withSelfRel());

        if(page.hasPrevious()){
            pagedModel.add(linkTo(methodOn(CommentController.class)
                    .getAll(searchContent,pageable.previousOrFirst())).withRel("prev"));
        }

        if(page.hasNext()){
            pagedModel.add(linkTo(methodOn(CommentController.class)
                    .getAll(searchContent,pageable.next())).withRel("next"));
        }

        pagedModel.add(linkTo(methodOn(CommentController.class)
                .getAll(searchContent, PageRequest.of(0,pageable.getPageSize(),pageable.getSort())))
                .withRel("first"));

        if(page.getTotalPages() > 1){
            pagedModel.add(linkTo(methodOn(CommentController.class)
                    .getAll(searchContent, PageRequest.of(page.getTotalPages()-1,pageable.getPageSize(),pageable.getSort())))
                    .withRel("last"));
        }

        return pagedModel;
    }
}
