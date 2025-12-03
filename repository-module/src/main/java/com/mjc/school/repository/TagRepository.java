package com.mjc.school.repository;

import com.mjc.school.repository.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long>, JpaSpecificationExecutor<Tag> {
    List<Tag> findAllByNewsId(Long newsId);
    boolean existsByNameIgnoreCase(String name);
    Long countByIdIn(Collection<Long> id);
}
