package com.mjc.school.repository.specification;

import com.mjc.school.repository.model.Author;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.util.StringUtils.hasText;

public class AuthorSpecification {

    public static Specification<Author> withSearch(String searchName){
        return (root,query,cb) ->
            (hasText(searchName))
                    ? cb.like(cb.lower(root.get("name")),"%"+searchName.toLowerCase()+"%")
                    : cb.conjunction();
    }

}
