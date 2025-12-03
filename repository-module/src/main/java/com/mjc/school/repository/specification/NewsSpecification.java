package com.mjc.school.repository.specification;

import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsSpecification {

    public static Specification<News> withSearch(List<String> tagNames,
                                                 List<Long> tagIds,
                                                 String authorName,
                                                 String title,
                                                 String content){
        return (root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);

            if((tagNames !=null && !tagNames.isEmpty()) ||
                    (tagIds !=null && !tagIds.isEmpty())) {

                Join<News, Tag> join = root.join("tags");

                if (tagNames != null && !tagNames.isEmpty()) {
                    predicates.add(join.get("name").in(tagNames));
                }
                if (tagIds != null && !tagIds.isEmpty()) {
                    predicates.add(join.get("id").in(tagIds));
                }
            }

            if(StringUtils.hasText(authorName)){
                Join<News, Author> join = root.join("author");
                predicates.add(cb.like(cb.lower(join.get("name")),"%"+authorName.toLowerCase()+"%"));
            }

            if(StringUtils.hasText(title)){
                predicates.add(cb.like(cb.lower(root.get("title")),"%"+title.toLowerCase()+"%"));
            }

            if(StringUtils.hasText(content)){
                predicates.add(cb.like(cb.lower(root.get("content")),"%"+content.toLowerCase()+"%"));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
