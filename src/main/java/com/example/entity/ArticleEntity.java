package com.example.entity;

import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "article")
@Setter
@Getter
public class ArticleEntity extends BaseStringEntity {
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "image_id", nullable = false)
    private String imageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private AttachEntity image;

    @Column(name = "region_id", nullable = false)
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "moderator_id", nullable = false)
    private Integer moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;

    @Column(name = "publisher_id", nullable = false)
    private Integer publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status = ArticleStatus.NOT_PUBLISHED;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private Set<ArticleTypesEntity> articleTypeSet;
}
