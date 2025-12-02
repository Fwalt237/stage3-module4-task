package com.mjc.school.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class News implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name="author_id")
    private Author author;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="news_tags",
    joinColumns={@JoinColumn(name="news_id")},
    inverseJoinColumns={@JoinColumn(name="tag_id")})
    private List<Tag> tags;

    @OneToMany(mappedBy="news", fetch=FetchType.LAZY)
    private List<Comment> comments ;
}
