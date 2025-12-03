package com.mjc.school.repository.specification;

import com.mjc.school.repository.model.Comment;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.util.StringUtils.hasText;

public class CommentSpecification {

    public static Specification<Comment> withSearch(String searchName){
        return (root,query,cb)->
                (hasText(searchName))
                        ? cb.like(cb.lower(root.get("content")),"%"+searchName.toLowerCase()+"%")
                        : cb.conjunction();
    }
}

