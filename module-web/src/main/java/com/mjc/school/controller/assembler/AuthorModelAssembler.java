package com.mjc.school.controller.assembler;


import com.mjc.school.controller.impl.AuthorController;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorModelAssembler {

    public EntityModel<AuthorDtoResponse> toModel(AuthorDtoResponse authorDtoResponse, Long id){

        var model = EntityModel.of(authorDtoResponse);

        model.add(linkTo(methodOn(AuthorController.class).getById(id)).withSelfRel());
        model.add(linkTo(methodOn(AuthorController.class).update(id,null)).withRel("update"));
        model.add(linkTo(methodOn(AuthorController.class).patch(id,null)).withRel("patch"));
        model.add(linkTo(AuthorController.class).slash(id).withRel("delete"));

        return model;
    }

    public PagedModel<EntityModel<AuthorDtoResponse>> toPagedModel(
            Page<AuthorDtoResponse> page,
            String searchName,
            Pageable pageable){

        var entityModels = page.map(dto->toModel(dto,dto.id()));

        var metadata =  new PagedModel.PageMetadata(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        var pagedModel = PagedModel.of(entityModels.getContent(),metadata);

        pagedModel.add(linkTo(methodOn(AuthorController.class)
                .getAll(searchName,pageable)).withSelfRel());

        if(page.hasPrevious()){
            pagedModel.add(linkTo(methodOn(AuthorController.class)
                    .getAll(searchName,pageable.previousOrFirst())).withRel("prev"));
        }

        if(page.hasNext()){
            pagedModel.add(linkTo(methodOn(AuthorController.class)
                    .getAll(searchName,pageable.next())).withRel("next"));
        }

        pagedModel.add(linkTo(methodOn(AuthorController.class)
                .getAll(searchName, PageRequest.of(0,pageable.getPageSize(),pageable.getSort())))
                .withRel("first"));

        if(page.getTotalPages() > 1){
            pagedModel.add(linkTo(methodOn(AuthorController.class)
                    .getAll(searchName, PageRequest.of(page.getTotalPages()-1,pageable.getPageSize(),pageable.getSort())))
                    .withRel("last"));
        }

        return pagedModel;
    }

}
