package com.mjc.school.controller.assembler;

import com.mjc.school.controller.impl.AuthorController;
import com.mjc.school.controller.impl.CommentController;
import com.mjc.school.controller.impl.NewsController;
import com.mjc.school.controller.impl.TagController;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NewsModelAssembler {

    public EntityModel<NewsDtoResponse> toModel(NewsDtoResponse newsDtoResponse, Long id){

        var model = EntityModel.of(newsDtoResponse);

        model.add(linkTo(methodOn(NewsController.class).getById(id)).withSelfRel());
        model.add(linkTo(methodOn(NewsController.class).update(id,null)).withRel("update"));
        model.add(linkTo(methodOn(NewsController.class).patch(id,null)).withRel("patch"));
        model.add(linkTo(methodOn(NewsController.class).deleteById(id)).withRel("delete"));
        model.add(linkTo(methodOn(AuthorController.class).getByNewsId(id)).withRel("author"));
        model.add(linkTo(methodOn(TagController.class).getByNewsId(id)).withRel("tags"));
        model.add(linkTo(methodOn(CommentController.class).getByNewsId(id)).withRel("comments"));

        return model;
    }

    public PagedModel<EntityModel<NewsDtoResponse>> toPagedModel(
            Page<NewsDtoResponse> page,
            List<String> tagNames,
            List<Long> tagIds,
            String authorName,
            String title,
            String content,
            Pageable pageable){

        var entityModels = page.map(dto->toModel(dto,dto.id()));

        var metadata = new PagedModel.PageMetadata(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        var pagedModel = PagedModel.of(entityModels.getContent(), metadata);

        pagedModel.add(linkTo(methodOn(NewsController.class).getAll(
                tagNames,tagIds,authorName,title,content,
                pageable)).withSelfRel());

        if(page.hasPrevious()){
            pagedModel.add(linkTo(methodOn(NewsController.class).getAll(
                    tagNames,tagIds,authorName,title,content,
                    pageable.previousOrFirst())).withRel("prev"));
        }

        if(page.hasNext()){
            pagedModel.add(linkTo(methodOn(NewsController.class).getAll(
                    tagNames,tagIds,authorName,title,content,
                    pageable.next())).withRel("next"));
        }

        pagedModel.add(linkTo(methodOn(NewsController.class).getAll(
                tagNames,tagIds,authorName,title,content,
                PageRequest.of(0,pageable.getPageSize(),pageable.getSort()))).withRel("first"));

        pagedModel.add(linkTo(methodOn(NewsController.class).getAll(
                tagNames,tagIds,authorName,title,content,
                PageRequest.of(page.getTotalPages()-1,pageable.getPageSize(),pageable.getSort()))).withRel("last"));

        return pagedModel;
    }

}
