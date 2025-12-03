package com.mjc.school.repository.specification;


import com.mjc.school.repository.model.Tag;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.util.StringUtils.hasText;

public class TagSpecification {

    public static Specification<Tag> withSearch(String searchName){
        return (root,query,cb)->
                (hasText(searchName))
                        ? cb.like(cb.lower(root.get("name")),"%"+searchName.toLowerCase()+"%")
                        : cb.conjunction();
    }
}

