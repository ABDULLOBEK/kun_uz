package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private String imageId;
    private Integer categoryId;
    private List<Integer> articleType;
    private Integer regionId;

    private RegionDTO region;
    private LocalDateTime createdDate;
}
